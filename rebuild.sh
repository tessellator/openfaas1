faas-cli remove hello-java
faas-cli build -f hello-java.yml
echo
echo next steps :
echo faas-cli deploy -f hello-java.yml --gateway http://127.0.0.1:8080
echo curl -s http://127.0.0.1:8080/function/hello-java --data-binary blablabla
