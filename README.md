# PageChangedDetector

PageChangedDetector allows you to choose a target site you want to keep an eye on and it looks for changes in its HTML body. The system performs its detection with regular intervals and when it finds any changes it notifies you that. The program is really simple, so far: it recognizes as a change also different tokens in <script> tag. If you want to help me to improve its quality, contact me.

## Getting Started

### Grab it

```
git clone git://github.com/CiroOl/PageChangedDetector.git
```

### Build it with Gradle

```
gradle build
```

### Run it

```
java -jar PageChangedDetector.jar
```
