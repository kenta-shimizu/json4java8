# json4java8

## Intruduction
This library is JSON ([RFC8259](https://tools.ietf.org/html/rfc8259)) parser implementation on Java8.


## Convert

### from POJO (Plain Old Java Objec) to JSON

```
/* to String */
String json = JsonHub.fromPojo(pojo).toJson();

/* to Writer */
Writer writer = new StringWriter()
JsonHub.fromPojo(pojo).toJson(writer);

/* to file */
Path path = Paths.get("path_of_file.json");
JsonHub.fromPojo(pojo).toJson(path);
```

### from JSON to POJO

```
/* from JSON String */
String json = "{\"num\": 100, \"str\": \"STRING\", \"bool\": true}";
Pojo pojo = JsonHub.fromJson(json).toPojo(Pojo.class);

/* from Reader */
Reader reader = new StringReader(json);
Pojo pojo = JsonHub.fromJson(json).toPojo(Pojo.class);

/* from file */
Path path = Paths.get("path_of_file.json");
Pojo pojo = JsonHub.readFile(path).toPojo(Pojo.class);
```

### from bytes to POJO
```
/* from bytes */
byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
Pojo pojo = JsonHub.fromBytes(bytes).toPojo(Pojo.class);

/* from InputStream */
InputStream strm = new ByteArrayInputStream();
Pojo pojo = JsonHub.fromBytes(strm).toPojo(Pojo.class);
```

## Pretty Print

```
/* to String */
String prettyPrintJson = JsonHub.fromPoso(pojo).prettyPrint();

/* write to file */
Path path = Paths.get("path_of_file.json");
JsonHub.fromPojo(pojo).prettyPrint(path);

/* write to writer */
Writer writer = new StringWriter();
JsonHub.fromPojo(pojo).prettyPrint(writer;
```

see also "example".


## Get Value

```
String json = "{
            "  \"num\":    100,
            "  \"str\":    \"STRING\",
            "  \"bool\":   true,
            "   \"array\":  [
                                "a",
                                "b",
                                "c"
                            ]
                ]
                }";

JsonHub jsonHub = JsonHub.fromJson(json);



```

Matrix of type, funciton

| Method | Object | Array | Number | String | true | false | null |
|---|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
|#get| x | y | x | z1|
|#get| x | y | x | z|
|#forEach(Consumer)| × | ○ | *1 | *2 |



See also ["/src/examples/example1/ExampleHttpGeneralServer.java"](/src/examples/example1/)

*1 : null

*2 : number


## Build JsonHub instance to create JSON

use JsonHubBuilder

```
JsonHubBuilder jhb = JsonHub.getBuilder();

JsonHub jsonHub = jhb.object(
    jhb.pair("num", 100),
    jhb.pair("str", "STRING"),
    jhb.pair("bool", true),
    jhb.pair("array", jhb.array(
        jhb.build("a"),
        jhb.build("b"),
        jhb.build("c")
    )
);

String json = jsonHub.toJson();

System.out.println(json);

/* {} */
```



