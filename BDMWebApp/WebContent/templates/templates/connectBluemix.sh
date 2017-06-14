echo connectBluemix java


# ...or Linux 64-bit binary
curl -L "https://cli.run.pivotal.io/stable?release=linux64-binary&source=github" | tar -zx
#permission for moving file
chmod -R 777 /usr/local/bin
# ...move it to /usr/local/bin or a location you know is in your $PATH
mv cf /usr/local/bin
# ...copy tab completion file on Ubuntu (takes affect after re-opening your shell)
sudo curl -o /usr/share/bash-completion/completions/cf https://raw.githubusercontent.com/cloudfoundry/cli/master/ci/installers/completion/cf
# ...and to confirm your cf CLI version
cf --version


#echo '' | sudo -S wget -O /etc/yum.repos.d/cloudfoundry-cli.repo https://packages.cloudfoundry.org/fedora/cloudfoundry-cli.repo - true

#echo '' | sudo -S yum install cf-cli - true

#dpkg -l | grep cf

#echo "connectBluemix java1"

#whereis -b cf

#echo "connectBluemix java2"

#dpkg --status cf