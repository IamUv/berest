# BeRest
![Maven Central](https://img.shields.io/maven-central/v/io.github.iamuv/berest?color=00DD00)
> 基于spring和jakarta.servlet的快速实现restful化API扩展
***
### 实现效果
\
一个只返回`String`的`GET`方式`Controller`：
```java
@GetMapping("/hello")
public String hello() {
    return "hello world";
}
```
\
`BeRest` `Simple`模式下的返回值：
```json
//默认配置
{
  "message": "hello world", //默认会将字符串返回值放入message
  "data": null 
} 
```
```json
//全显配置
{
  "ID": "1921682235-1666451353-504573122",
  "status": 200,
  "message": "hello world",
  "data": null
}
```
\
返回`Map`的`GET`：
```java
@GetMapping("/map")
public Map<Object, Object> map() {
    return Map.of("username", "admin", "userId", "1");
}
```
\
`BeRest` `Simple`模式下的返回值：
```json
//默认配置
{
  "message": "success",
  "data": {
    "username": "admin",
    "userId": "1"
  }
}
```
```json
//全显配置
{
  "ID": "1921682235-1666496947-1140019921",
  "status": 200,
  "message": "success",
  "data": {
    "username": "admin",
    "userId": "1"
  }
}
```
*可以看到，`BeRest`扩展是无侵入式的*\
*在`Controller`方法的返回值上进行二次封装*\
*将返回值结构统一标准化*
***

### 快速开始
基于`Spring`开箱即用，引入依赖后自动装填缺省配置\
内置`Simple`和`Standard`两种模式\
默认使用`Simple`模式

![Maven Central](https://img.shields.io/maven-central/v/io.github.iamuv/berest?color=00DD00)
```xml
<dependency>
    <groupId>io.github.iamuv</groupId>
    <artifactId>berest</artifactId>
    <version>0.5.0</version>
</dependency>
```
`gradle`
```xml
implementation group: 'io.github.iamuv', name: 'berest', version: '0.5.0'
```
***

### `HTTP`协议中的`Status Code`
\
返回值中的`status`字段与HTTP协议中的`Status Code`保持一致
```http request
Request Method: GET
Status Code: 200 OK
```
\
其余请求方式的默认的`Status Code`
```http request
Request Method: POST
Status Code: 201 Created
```
```http request
Request Method: PUT
Status Code: 201 Created
```
```http request
Request Method: DELETE
Status Code: 201 Created
```
\
当`Controller`的方法返回值为`void`时默认的`Status Code`
```http request
Request Method: GET
Status Code: 200 OK
```
```http request
Request Method: POST
Status Code: 201 Created
```
```http request
Request Method: PUT
Status Code: 204 No Content
```
```http request
Request Method: DELETE
Status Code: 205 Reset Content
```
*配置文件中可以修改这七种情况下返回的默认`Status Code`*
***

### 关于`异常`的处理
\
当业务`失败`或者`出现异常`情况时，用`throw exception`的方式来代替`return`\
`BeRest`会`捕获异常`并封装为`失败`的返回值并重新修改`HTTP`协议中的`Status Code`

```java
@GetMapping("/error")
public void error() {
    throw new NullPointerException();
}
```
``Simple``模式下的返回值
```json
//默认配置
{
  "message": "500 Internal Server Error.",
  "data": "NullPointerException"
}
```
``Standard``模式下的返回值
```json
//默认配置
{
  "success": false,
  "error": {
    "message": "500 Internal Server Error.",
    "description": "The server has encountered a situation it does not know how to handle.",
    "exception": "NullPointerException"
  },
  "timer": {
    "request": "2024-02-01T16:10:32.3767488",
    "response": "2024-02-01T16:10:32.408749",
    "duration": 32
  },
  "code": null,
  "message": "failure",
  "data": null
}
```
``HTTP``信息
```http request
Request Method: GET
Status Code: 500 Internal Server Error
```
获取`异常`中的`信息`
```java
@GetMapping("/error")
public void error() {
    throw new NullPointerException("This is a null");
}
```
```json
{
  "success": false,
  "error": {
    "message": "This is a null",
    "description": "The server has encountered a situation it does not know how to handle.",
    "exception": "NullPointerException"
  },
  "timer": {
    "request": "2024-02-01T16:32:25.1874356",
    "response": "2024-02-01T16:32:25.220435",
    "duration": 33
  },
  "code": null,
  "message": "failure",
  "data": null
}
```
自定义``异常``
```java
@GetMapping("/error") 
public void error() throws StandardException {
    throw new StandardException("test error", "500.1", "This is a test of error ");
}
```
``Standard``模式下的返回值
```json
{
  "success": false,
  "error": {
    "message": "test error",
    "description": "This is a test of error ",
    "exception": "StandardException"
  },
  "timer": {
    "request": "2024-02-01T18:54:26.2391619",
    "response": "2024-02-01T18:54:26.2671618",
    "duration": 28
  },
  "code": "500.1",
  "message": "failure",
  "data": null
}
```
***
### 对``Spring``的支持
已支持的异常：\
``ErrorResponse`` \
``BeansException`` \
``NestedRuntimeException``\
``BindException`` 
***
### 对``JSR-303``的支持
支持``@Valid``和``Spring``变种``JSR-303``的``@Validated``
```java
class Entity {

    @NotEmpty
    @Size(message = "姓名长度必须在{min}到{max}之间", min = 8, max = 30)
    private String name;

    @Min(message = "年龄必须大于{value}", value = 18)
    private int age;

    @NotEmpty
    @Email
    private String email;
}
``` 
```http request
Request Method: PUT
Body:
{
  "name": "test",
  "age": 1,
  "email": "myemail"
}
```
``Simple``模式下的返回值
```json
{
  "message": "年龄必须大于18;姓名长度必须在8到30之间;不是一个合法的电子邮件地址;",
  "data": [
    {
      "codes": [
        "Min.entity.age",
        "Min.age",
        "Min.int",
        "Min"
      ],
      "arguments": [
        {
          "codes": [
            "entity.age",
            "age"
          ],
          "arguments": null,
          "defaultMessage": "age",
          "code": "age"
        },
        18
      ],
      "defaultMessage": "年龄必须大于18",
      "objectName": "entity",
      "field": "age",
      "rejectedValue": 1,
      "bindingFailure": false,
      "code": "Min"
    },
    {
      "codes": [
        "Size.entity.name",
        "Size.name",
        "Size.java.lang.String",
        "Size"
      ],
      "arguments": [
        {
          "codes": [
            "entity.name",
            "name"
          ],
          "arguments": null,
          "defaultMessage": "name",
          "code": "name"
        },
        30,
        8
      ],
      "defaultMessage": "姓名长度必须在8到30之间",
      "objectName": "entity",
      "field": "name",
      "rejectedValue": "test",
      "bindingFailure": false,
      "code": "Size"
    },
    {
      "codes": [
        "Email.entity.email",
        "Email.email",
        "Email.java.lang.String",
        "Email"
      ],
      "arguments": [
        {
          "codes": [
            "entity.email",
            "email"
          ],
          "arguments": null,
          "defaultMessage": "email",
          "code": "email"
        },
        [

        ],
        {
          "arguments": null,
          "codes": [
            ".*"
          ],
          "defaultMessage": ".*"
        }
      ],
      "defaultMessage": "不是一个合法的电子邮件地址",
      "objectName": "entity",
      "field": "email",
      "rejectedValue": "myemail",
      "bindingFailure": false,
      "code": "Email"
    }
  ]
}
```