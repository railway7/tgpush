## TGPUSH

Use [YuQ](https://github.com/YuQWorks/YuQ) and [TelegramBots](https://github.com/rubenlagus/TelegramBots)

Use tg to push messages


## api

```text
# send markdown msg
http://localhost:8081/md/{key}
params: msg
```

```text
# send photo
http://localhost:8081/image/{key}
params: url or base
```

## command

* /new - create a key
* /regen - regen a key
* /query - query key