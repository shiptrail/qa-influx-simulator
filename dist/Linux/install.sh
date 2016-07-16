#!/usr/bin/bash
if type "apt-get" > /dev/null; then
  sudo apt-get update
  sudo apt-get install gpsbabel
  exit 0
fi

if type "pacman" > /dev/null; then
  sudo pacman -Syy
  sudo pacman -S gpsbabel
  exit 0
fi

echo "You are using a weird distro!"
echo "You may either install gpsbabel via your package manager"
echo "or use the following commands to build gpsbabel from source:"
echo "mkdir -p src"
echo "tar -xzvf gpsbabel-1.5.3.tar.gz -C src"
echo "cd src"
echo "./configure"
echo "make"
echo "make install"
