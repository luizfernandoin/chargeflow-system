#!/bin/bash
set -euo pipefail

echo "========================================"
echo "🚀  DEPLOY CHARGEFLOW - SEQUÊNCIA CORRETA"
echo "========================================"

MANAGER_IP="192.168.56.10"
REGISTRY="$MANAGER_IP:5000"
PROXY_URL="http://$MANAGER_IP:8080"
WSDL_URL="$PROXY_URL/ws/chargeservice.wsdl"

# Carregar variáveis do .env
if [ -f .env ]; then
    echo "📋 Carregando variáveis do .env..."
    # Fazer parsing correto do .env (ignorando comentários e linhas vazias)
    while IFS='=' read -r key value; do
        [[ "$key" =~ ^#.*$ ]] && continue
        [[ -z "$key" ]] && continue
        export "$key=$value"
    done < .env
else
    echo "⚠️  Arquivo .env não encontrado. Usando valores padrão."
fi


# ============================================
# 1. CONFIGURAR DOCKER NO HOST
# ============================================
echo "🔧 Configurando Docker no host..."

DOCKER_DAEMON_CONFIG="/etc/docker/daemon.json"

# Configurar insecure registry
if [ -f "$DOCKER_DAEMON_CONFIG" ]; then
    sudo cp "$DOCKER_DAEMON_CONFIG" "${DOCKER_DAEMON_CONFIG}.backup.$(date +%Y%m%d_%H%M%S)"
    echo "{\"insecure-registries\": [\"$REGISTRY\"]}" | sudo tee "$DOCKER_DAEMON_CONFIG"
else
    echo "{\"insecure-registries\": [\"$REGISTRY\"]}" | sudo tee "$DOCKER_DAEMON_CONFIG"
fi

echo "🔄 Reiniciando Docker..."
sudo systemctl restart docker 2>/dev/null || sudo service docker restart
sleep 3

cd vagrant

echo "1️⃣  Subindo VMs..."
vagrant up

echo ""
echo "⏳ Aguardando registry..."
until curl -sf "http://$REGISTRY/v2/_catalog" >/dev/null; do
    sleep 2
done
echo "✅ Registry disponível"

echo ""
echo "🔨 2. Build e deploy do PROXY primeiro..."

# Build do proxy
echo "🔧 Build charge-proxy..."
docker build -t "$REGISTRY/charge-proxy:latest" ../charge-proxy
docker push "$REGISTRY/charge-proxy:latest"

echo "📦 Deploy inicial (banco + proxy)..."
vagrant ssh manager -- "
  cd /vagrant/stacks
  echo '📦 Banco de dados...'
  docker stack deploy -c db.yml chargeflow
  sleep 3
  
  echo '📦 Proxy...'
  docker stack deploy -c charge-proxy.yml chargeflow
  echo '⏳ Aguardando proxy subir...'
  sleep 15
"

echo ""
echo "📥 4. Baixando WSDL do proxy..."
WSDL_DIR="../charge-manager/src/main/resources/wsdl"
WSDL_FILE="$WSDL_DIR/charge-proxy.wsdl"

echo "   Diretório atual: $(pwd)"
echo "   Diretório destino: $WSDL_DIR"

mkdir -p "$WSDL_DIR"

if curl -sf "$WSDL_URL" -o "$WSDL_FILE"; then
    echo "✅ WSDL baixado com sucesso para: $WSDL_FILE"
fi

echo ""
echo "🔧 Build charge-manager..."
docker build -t "$REGISTRY/charge-manager:latest" ../charge-manager
docker push "$REGISTRY/charge-manager:latest"

echo ""
echo "📦 3. Deploy do manager..."

# Copiar .env para a VM (via pasta stack sincronizada)
if [ -f .env ]; then
    echo "📋 Copiando .env para a VM..."
    cp .env ../stack/.env
fi

vagrant ssh manager -- "
  cd /vagrant/stacks
  echo '🔧 Substituindo variáveis no YAML...'
  # Carregar variáveis do .env de forma segura
  while IFS='=' read -r key value; do
    [[ \"\$key\" =~ ^#.*\$ ]] && continue
    [[ -z \"\$key\" ]] && continue
    export \"\$key=\$value\"
  done < .env
  envsubst < charge-manager.yml > charge-manager-resolved.yml
  echo '📦 Manager...'
  docker stack deploy -c charge-manager-resolved.yml chargeflow
  docker stack ps chargeflow
"

echo ""
echo "========================================"
echo "🎉  SISTEMA IMPLANTADO!"
echo "========================================"
echo ""
echo "🌐 Endpoints:"
echo "  - Manager API:    http://$MANAGER_IP:8081"
echo "  - Proxy:          http://$MANAGER_IP:8080"
echo "  - WSDL Proxy:     http://$MANAGER_IP:8080/ws/chargeservice.wsdl"