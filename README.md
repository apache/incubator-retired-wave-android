Apache Wave Client for Android
-------------------------------

"Wave in a Box" (WIAB) project is a stand alone wave server and rich web client that serves as an Apache Wave reference implementation.
Apache Wave site: http://incubator.apache.org/wave/
This project lets developers and enterprise users run wave servers and host waves on their own hardware. And then share those waves with other wave servers.

Build
-----

The Android client requires Java 7, and an Android device running Android 5.0 or higher.

The client can be built using gradle, from the command line, or via an IDE (IDEA files can be found in this repository).

You will need to install the Android SDK first, the latest version can always be found at https://developer.android.com/sdk/index.html.

To build everything, run:
    gradle assemble

To run the tests, run:
    gradle check

A full list of gradle tasks can be found by running:
    gradle tasks

To learn more about Wave and the Wave Protocols:
------
1. Subscribe to the wave-dev mailing list, find instructions at http://incubator.apache.org/wave/mailing-lists.html.
2. Take a look at the developer docs at https://incubator.apache.org/wave/documentation.html
3. Visit the Apache Wave wiki at https://cwiki.apache.org/confluence/display/WAVE/Home.

