# Http-Record

项目初衷是在开发时和第三方集成，通常有些回调在测试阶段需要接收参数进行调试，此项目就是为了在公网记录请求信息。  

当然，通常在本地进行内网穿透后，也可以达到相同目的。  

另一方面，本项目也作为 kotlin native 的尝试，故而诞生。

## 使用

### Path

- GET `/history` 最近50条请求记录
- ALL `/*`
  - `参数` echo: 任意字符串
  - `参数` type: 类型 `json`、`text`

### 使用示例
```curl
curl --location --request POST 'localhost:8080/record?echo={%22err_no%22:0,%22err_tips%22:%22success%22}&type=json

# response
application/json,
{"err_no":0,"err_tips":"success"}
```

## 安装

### docker 

```shell
$ docker run sivan757/http-record:latest
```

### jar

```shell
$ gradle shadowJar
```

### native

```shell
$ gradle shadowJar

$ ./build.sh
```
需要 GraalVm 并安装 native-image，编译产物仅可运行在编译平台。

### release

在本项目 `release` 中，选取对应平台编译后原生程序，直接启动即可。
