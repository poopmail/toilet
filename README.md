# toilet

Toilet is the mail receiver for poop mail. Its purpose is to forward any valid received emails to the canalization using Redis pub-sub.

### Why do you use Java?

You don't want me to write Go applications, trust me. Yes, we could have implemented the mail receiver in the canalization project, but then I'd have
nothing to do :)

### Credits and third party licenses

MimeMessageParser from [Apache commons-email](https://github.com/apache/commons-email): \
[Apache License 2.0](https://github.com/apache/commons-email/blob/master/LICENSE.txt)

[SubEtha SMTP](https://github.com/voodoodyne/subethasmtp): \
[Apache License 2.0](https://github.com/voodoodyne/subethasmtp/blob/master/LICENSE.txt)

[Lettuce](https://github.com/lettuce-io/lettuce-core): \
[Apache License 2.0](https://github.com/lettuce-io/lettuce-core/blob/main/LICENSE)

[Gson](https://github.com/google/gson): \
[Apache License 2.0](https://github.com/google/gson/blob/master/LICENSE)