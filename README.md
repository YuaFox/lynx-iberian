# Lynx-Iberian

## How to use

[Install node-red dependency](https://github.com/YuaFox/node-red-contrib-lynx-iberian) to manage the bot.

Run the bot with docker-compose.yml
```yaml
version: '3.9'

services:
    lynx:
        image: lynx-iberian:2023.1
        ports:
            - "8080:8080"
        volumes:
            - ./config:/config
            - ./plugins:/app/plugins
        environment:
            DB_URL: "mysql://username:password@172.17.0.1/lynxiberian?createDatabaseIfNotExist=true"
            LYNX_USERNAME: user
            LYNX_PASSWORD: password
            NODERED_HOST: 172.17.0.1
            # Optional: Only if using twitter plugin
            TWITTER_KEY: ...
            TWITTER_SECRET: ...
```