# Promise

Promise is based on Javascripts(not-native) Promise implementation

##Usage
```Java
Defered<String, String, String> defer = new Defered();

defer.then(new Callback<String>(){
  @Override
  public void call(String argument) {
    logger.info(argument);
  }
});
```
