echo connectBluemix java

echo '' | sudo -S wget -O /etc/yum.repos.d/cloudfoundry-cli.repo https://packages.cloudfoundry.org/fedora/cloudfoundry-cli.repo

echo '' | sudo -S yum install cf-cli

dpkg -l | grep cf

#echo "connectBluemix java1"

#whereis -b cf

#echo "connectBluemix java2"

#dpkg --status cf