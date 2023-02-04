# Lynx-Iberian

## Install

Get the example project https://github.com/YuaFox/lynx-iberian-example and follow the instructions.

## How to use

### Install media
1. Put your images in config/local
2. Start lynx-iberian

### Configure flows
1. Go to node-red ( http://localhost:1880 )
2. Configure lynx-server using the credentials from docker-compose

Host is 172.17.0.1, dont use localhost since is a docker container, port is 6000

3. Configure a flow like this: input -> explorer -> pusblisher -> output

### Available drivers

#### Input
- minutely
- hourly
- daily
- monthly

#### Explorer
- **random**: Get a random media

#### Publisher
- **telegram**: Post on telegram
- **mastodon**: Post on mastodon
