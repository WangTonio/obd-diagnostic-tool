# Introduction #

The OBD Mobile Tool is an android application that interacts with an automobiles ECU (Engine Control Unit). The application will allow the user to view real time data such as vehicle speed, rpm, temperature, among others. A possible list of data that can viewed in the application is available at
[OBD-II Standard](http://en.wikipedia.org/wiki/OBD-II_PIDs) .  <br>
<b>NOTE</b> that not all commands in the OBD-II Standard will be implemented.<br>
The Bluetooth device that will be used during the development of the application will be:<br>
<a href='http://www.amazon.com/Yongtek-Bluetooth-Diagnostic-Scanner-Wireless/dp/B0076KBPNI/ref=sr_1_1/180-0002807-3700707?ie=UTF8&qid=1394008736&sr=8-1&keywords=obd+elm327'>ELM-327-Bluetooh-Device</a> May Change.<br>
<br>
<h1>Details</h1>

The application will have five(maybe more or less) screens:<br>
<ul><li>Main Page, loaded when app is open.<br>
<ul><li>Button to Connect OBD-II Bluetooth Device<br>
</li></ul></li><li>Live Data ( Possibly with visual Gauges for some data like rpm speed and temp)<br>
</li><li>Diagnostic Trouble Codes(DTC) Look up and deletion<br>
</li><li>Freeze Frame Data (A snap shot of data when a emission-related diagnostic trouble code occurs)<br>
Scroll bar on each screen is used for navigation. Swapping left or right will move you to the next screen, excluding the settings page.<br>
<h1>Mockups</h1>
<h3>Main Screen</h3>
<a href='http://imgur.com/idJL702'><img src='http://i.imgur.com/idJL702.png' height='480' /></a><br>
The top scroll bar will be used to navigate through the application. The center status button/label will be used to connect and show the status of a OBD device.<br>
<h3>Settings</h3>
<a href='http://imgur.com/3RkJgr2'><img src='http://i.imgur.com/3RkJgr2.png' height='480' /></a><br>
Just a basic settings screen that will allow users to set attributes<br>
for the application. For example, temps in F or C, and speed in MPH or kM/h.<br>
<h3>Live Data</h3>
<a href='http://imgur.com/eD8EcxP'><img src='http://i.imgur.com/eD8EcxP.png' width='480' /></a><br>
Show live data dyring vehicle operation. Some values that will be shown are speed, rpm, temperator, throttle position, and maybe more.<br>
<h3>DTC Code Look up/Deletion</h3>
<a href='http://imgur.com/xuiTETq'><img src='http://i.imgur.com/xuiTETq.png' height='480' /></a><br>
A list of trouble codes(if any are present). A button at the bottom<br>
to delete current DTC on the ECU.<br>
<h3>Freeze Frame</h3>
<a href='http://imgur.com/6cQXTVj'><img src='http://i.imgur.com/6cQXTVj.png' height='480' /></a><br>
Shows current frozen data that is in the ECU when a DTC occurs. Only emission-related diagnostic trouble codes will trigger DTC freeze frame stores.<br>
<h1>Typical use</h1>
A typical scenario of using the OBD application is a user who wants to determine why his check engine light is on. With the OBD application, a user can attach a OBD Bluetooth device-that is bought separately- to their car. Once connected a user can see current data that is logged on their cars ECU(engine control unit). For example, to determine why their engine light is on, they can scroll to the DTC Look Up screen. On this screen, Current trouble codes will be displayed. A example of a code would be a unique number like P0300, and a short description, Random/Multiple Cylinder Misfire. With the information gathered, a user can then go to Google and look up details about the trouble code, and ways to remedy it.