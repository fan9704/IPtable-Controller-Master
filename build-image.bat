del ./target/*.jar
mvn package
docker build . -t fan9704/iptable_controller_master
docker push fan9704/iptable_controller_master