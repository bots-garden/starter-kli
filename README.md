

## Install Extism

```bash
# -----------------------
# Install Extism
# -----------------------
sudo apt-get update -y
sudo apt-get install -y pkg-config

sudo apt install python3-pip -y
pip3 install poetry
pip3 install git+https://github.com/extism/cli

echo "export EXTISM_HOME=\"\$HOME/.local\"" >> ${HOME}/.bashrc
echo "export PATH=\"\$EXTISM_HOME/bin:\$PATH\"" >> ${HOME}/.bashrc

source ${HOME}/.bashrc

extism --prefix=/usr/local install latest
pip3 install extism
```

## Run the demo

```bash
LD_LIBRARY_PATH="/usr/local/lib" \
WASM_FILE="/home/ubuntu/kotlin-wasm-runner/starter-kli/go-handler-plugin/simple.wasm" \
mvn clean compile exec:java
```
