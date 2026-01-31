#!/bin/bash
# ============================================
# 🔄 REBUILD RÁPIDO - Apenas apps, sem VMs
# ============================================
set -euo pipefail

MANAGER_IP="192.168.56.10"
REGISTRY="$MANAGER_IP:5000"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "========================================"
echo "🔄 REBUILD RÁPIDO - Charge Services"
echo "========================================"

# Validar se as VMs estão rodando
echo "🔍 Verificando se VMs estão rodando..."
cd "$SCRIPT_DIR/vagrant"

if ! vagrant status manager | grep -q "running"; then
    echo "❌ VMs não estão rodando!"
    echo "   Execute primeiro: ./start.sh"
    exit 1
fi

echo "✅ VMs estão ativas"

# Configurar insecure registry no host
echo ""
echo "🔧 Configurando Docker no host..."
DOCKER_DAEMON_CONFIG="/etc/docker/daemon.json"
if [ -f "$DOCKER_DAEMON_CONFIG" ]; then
    sudo cp "$DOCKER_DAEMON_CONFIG" "${DOCKER_DAEMON_CONFIG}.backup.$(date +%Y%m%d_%H%M%S)"
fi
echo "{\"insecure-registries\": [\"$REGISTRY\"]}" | sudo tee "$DOCKER_DAEMON_CONFIG" > /dev/null
sudo systemctl restart docker 2>/dev/null || sudo service docker restart > /dev/null 2>&1 || true
sleep 2

# Verificar registry
echo "⏳ Aguardando registry..."
for i in {1..30}; do
    if curl -sf "http://$REGISTRY/v2/_catalog" >/dev/null 2>&1; then
        echo "✅ Registry disponível"
        break
    fi
    if [ $i -eq 30 ]; then
        echo "⚠️  Registry não respondeu, continuando..."
        break
    fi
    sleep 1
done

cd "$SCRIPT_DIR"

# ============================================
# BUILD CHARGE-PROXY
# ============================================
echo ""
echo "🔨 [1/3] Building charge-proxy..."
docker build \
    --tag "$REGISTRY/charge-proxy:latest" \
    --file charge-proxy/Dockerfile \
    charge-proxy

echo "📤 Pushing charge-proxy..."
docker push "$REGISTRY/charge-proxy:latest" > /dev/null
echo "✅ charge-proxy pronto"

# ============================================
# BUILD CHARGE-MANAGER
# ============================================
echo ""
echo "🔨 [2/3] Building charge-manager..."
docker build \
    --tag "$REGISTRY/charge-manager:latest" \
    --file charge-manager/Dockerfile \
    charge-manager

echo "📤 Pushing charge-manager..."
docker push "$REGISTRY/charge-manager:latest" > /dev/null
echo "✅ charge-manager pronto"

# ============================================
# DEPLOY NAS VMs
# ============================================
echo ""
echo "🚀 [3/3] Atualizando serviços nas VMs..."

cd vagrant

# Proxy
echo "   Atualizando charge-proxy..."
vagrant ssh manager -- "
  docker service update \
    --image $REGISTRY/charge-proxy:latest \
    chargeflow_charge-proxy 2>/dev/null || \
  (cd /vagrant/stacks && docker stack deploy -c charge-proxy.yml chargeflow)
" > /dev/null 2>&1 || true

sleep 3

# Manager
echo "   Atualizando charge-manager..."
vagrant ssh manager -- "
  docker service update \
    --image $REGISTRY/charge-manager:latest \
    chargeflow_charge-manager 2>/dev/null || \
  (cd /vagrant/stacks && docker stack deploy -c charge-manager.yml chargeflow)
" > /dev/null 2>&1 || true

echo ""
echo "========================================"
echo "✅ REBUILD COMPLETO!"
echo "========================================"
echo ""
echo "📊 Status dos serviços:"
vagrant ssh manager -- "docker stack services chargeflow" || true
echo ""
echo "⏱️  Próximos rebuilds serão ainda mais rápidos (cache Docker)!"
