# dslink-scala-microburst
A DSLink generating bursty but low average traffic.

This DSLink updates a node in bursty manner.  It has one Node named Bursty.  Bursty has integer value.
It updates its value every second incrementally.  It increments its value given time every second.
When user gives 5 in command line argument, it increments its value 5 time bursty every second.
Subscriber of the Node will get 0, 1, 2, 3, 4 one after another then 1 second interval then get 5, 6, 7, 8, 9
and so on.

This DSLink is useful when you debug your end-to-end DSA data flow is robust enough in traffic burst.


# Usage

```shell-session
$ sbt assembly
$ java -jar target/scala-2.12/dslink-scala-microburst-assembly-0.1.0-SNAPSHOT.jar <number of burst output>  --broker=https://broker-host:port/conn
```

## Permission note

Starting with Cisco Kinetic Edge and Fog Processing Module 1.2.1, permission feature is enable by default for new
installation.  With such default permission setting, broker permits nothing to DSLink connected from external host.
This is inconvenient when you run broker in a Docker container and you run your DSLink under development on host OS or
another Docker container.  To make broker permissive for external DSLink, you have to change server.json.

There is two simple way to permit everything for external DSLink.  One is making "defaultPermission" empty.

```json
"defaultPermission": null
```

The other way is changing value of "default" entry "config" from "none".

```json
"defaultPermission": [
  [":config","config"],
  [":write","write"],
  [":read","read"],
  [":user","read"],
  [":trustedLink","config"],
  ["default","config"]
```

Note that the above is not recommended permission for production.
