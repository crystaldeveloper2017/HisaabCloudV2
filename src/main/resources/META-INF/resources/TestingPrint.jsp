<!DOCTYPE html>
<!-- saved from url=(0022)https://rawbt.ru/app2/ -->
<html lang="en"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    
    <title>In-APP browser</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <script src="./Test_files/iconv-lite.bundle.js.download" charset="utf-8"></script>
    <link rel="stylesheet" href="./Test_files/app.css">
</head>
<body style="max-width:640px" cz-shortcut-listen="true">
<div style="padding: 8px">
<center>
<b>You can set your start page.</b><br>
</center><br>

<p>After installing the program and configure the printer connection, the program will simply work and do it without exaggerating its importance, just a small icon   in the notification bar.</p>
<div id="switch">
    <a style="color:dodgerblue;text-decoration: none; border-bottom:1px dashed dodgerblue" onclick="document.getElementById(&#39;switch&#39;).style.display=&#39;none&#39;;document.getElementById(&#39;more&#39;).style.display=&#39;block&#39;;">More about menu and functions</a>
<br><br>
</div>
    <div id="more" style="display:none">
<b>You can easily print texts and images from your phone.</b>
<p>Find menu items "share", "send" or "open" in you app, click and select RawBT
    (Gallery,FileManager and many other application).</p>

<p><b>RawBT menu</b></p>
<p>Simple tools have been added for setting up the printer and if you no have any programs with "share".</p>
<ul>
    <li>Home - this text</li>
    <li>Edit - Simple editor.</li>
    <li>Txt - Allows print as UTF-8 or 1-byte encoding</li>
    <li>PDF - file choice to print</li>
    <li>IMG - with preview</li>
    <li>Settings - configure printer</li>
    <li>License - remove free adds</li>
</ul>


<b>RawBT also print service</b>
<p>This program is Freemium. The difference between the versions (free and premium) in the output on the printout of the text that is printed in the free version.
   
</p>
</div>
</div>


<div id="demo" style="display: block;">
<header id="header" class="header">
    <div class="container">
        <div class="branding">
            PRESENT JAVASCRIPT
            <h1 style="margin:0;padding: 0">
                <span class="text-highlight">RAWBT</span> <span class="text-bold">Library</span>
            </h1>
        </div>
    </div>
</header>
<br>
<div style="padding: 8px">
     <fieldset><legend>Printer family</legend>
         <label><input type="radio" name="pf" checked="" value="GENERAL" onclick="CodePageType =&#39;GENERAL&#39;;">Epson(General)</label>
         <label><input type="radio" name="pf" value="PT210" onclick="CodePageType = &#39;PT210&#39;;">Goojprt PT-210</label>
     </fieldset>

    <h2>Demo</h2>
    <button class="green" onclick="fontDemo();return false;">Font</button>
    <button class="green" onclick="alignDemo();return false;">Align</button>
    <button class="green" onclick="decorDemo();return false;">Decor</button>
    <button class="green" onclick="encodingDemo();return false;">Encode</button>

    <h2>Text Tool
    <select id="lang" style="float:right" onchange="userLang=this.value">
    <option value="sq">Albanian</option><option value="bg">Bulgarian</option><option value="ca">Catalan</option><option value="zh">Chinese</option><option value="hr">Croatian</option><option value="cs">Czech</option><option value="da">Danish</option><option value="nl">Dutch</option><option value="en">English</option><option value="et">Estonian</option><option value="fi">Finnish (Suomi)</option><option value="fr">French</option><option value="de">Deutsch</option><option value="el">Greek</option><option value="hu">Hungarian</option><option value="it">Italian</option><option value="id">Indonesian</option><option value="lt">Lithuanian</option><option value="ms">Malay</option><option value="no">Norwegian</option><option value="pl">Polish</option><option value="pt">Portuguese</option><option value="ru">Russian</option><option value="sk">Slovak</option><option value="sl">Slovenian</option><option value="es">Spanish</option><option value="sv">Swedish</option><option value="th">Thai</option><option value="tr">Turkish</option><option value="uk">Ukrainian</option></select>
    </h2>
    <div style="clear:both"></div>
    <form onsubmit="return false;">
        <textarea id="demotext" rows="8" style="width:100%;box-sizing: border-box;padding: 8px"></textarea>
        <button type="reset" value="Reset">Clear</button>
        <button onclick="document.getElementById(&#39;demotext&#39;).value = loremIpsum(userLang);return false;">Lorem</button>
        <button onclick="document.getElementById(&#39;demotext&#39;).value = pangrams(userLang);return false;">Pangram</button>
        <br>
        <fieldset><legend>Align</legend>
            <label style="width:35%;display: inline-block"><input type="radio" name="align" value="0" checked="">Left</label>
            <label style="width:35%;display: inline-block"><input type="radio" name="align" value="1">Center</label>
            <label style="display: inline-block"><input type="radio" name="align" value="2">Right</label>
            <br><small style="color: grey">ESC a - select justification</small>
        </fieldset>
        <br>
        <fieldset><legend>Font</legend>
            <label style="margin-bottom:8px;display: inline-block;width:90%"><input type="checkbox" name="font" value="1">FONT B (else font A as default)</label>
            <label style="width:45%;margin-bottom:8px;display: inline-block"><input type="checkbox" name="font" value="16">Double height</label>
            <label style="width:45%;margin-bottom:8px;display: inline-block"><input type="checkbox" name="font" value="32">Double width</label>
            <label style="width:45%;margin-bottom:8px;display: inline-block"><input type="checkbox" name="font" value="8">Empjasized</label>
            <label style="width:45%;margin-bottom:8px;display: inline-block"><input type="checkbox" name="font" value="128">Underline</label>
            <label style="width:90%;margin-bottom:8px;display: inline-block"><input type="checkbox" name="font" value="64">Italic (any printers, not all)</label>

            <br><small style="color: grey">ESC ! - select print mode</small>
        </fieldset>

        <br>
        <button class="green" style="width:100%;margin:0" onclick="printDemoText(); return false;">Print</button>
    </form>

<br>

    <h2>Bar Code
        <select id="bartype" style="float:right" onchange="democode(this)">
            <option value="65" data-example="036000291452">UPCA</option>
            <option value="66" data-example="1234567">UPCE</option>
            <option value="67" data-example="012345678901">JAN13</option>
            <option value="68" data-example="0123456">JAN8</option>
            <option value="69" selected="" data-example="ABCZ012">CODE39</option>
            <option value="70" data-example="0123456789">ITF</option>
            <option value="71" data-example="A012$+-./:A">CODABAR</option>
            <option value="72" data-example="012.ABZ">CODE93</option>
            <option value="73" data-example="012z !.,">CODE128</option>
        </select>
    </h2>
    <div style="clear:both"></div>
    <form>
        <input id="demobarcode" style="width:100%;box-sizing: border-box;padding:8px;border:1px solid #dddddd" value="">
        <button type="reset" value="Reset">Clear</button>
        <br>
        <fieldset><legend>HRI</legend>
            <label style="width:35%;display: inline-block"><input type="radio" name="hri" value="0" checked="">None</label>
            <label style="width:35%;display: inline-block"><input type="radio" name="hri" value="1">Above</label>
            <label style="display: inline-block"><input type="radio" name="hri" value="2">Below</label>
        </fieldset>
        <br>
        <fieldset><legend>Size</legend>
            Height: <span id="show_h">162</span> <input id="barcode_h" type="range" min="1" max="255" value="162" style="width:255px" onchange="document.getElementById(&#39;show_h&#39;).innerText=this.value">
        </fieldset>

        <br>
        <button class="green" style="width:100%;margin:0" onclick="printDemoBarCode(); return false;">Print</button>
    </form>


    <br>

    <h2>QR Code
    </h2>
    <div style="clear:both"></div>
    <form>
        <textarea id="demoqrcode" style="width:100%;box-sizing: border-box;padding:8px;border:1px solid #dddddd">https://rawbt.402d.ru/app2/index.php</textarea>
        <br>
        <fieldset>
            E: <select id="QR_EC" style="padding: 4px">
                <option value="0" selected="">L</option>
                <option value="1">M</option>
                <option value="2">Q</option>
                <option value="3">H</option>
            </select>
            S: <select id="QR_SIZE" style="padding: 4px">
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3" selected="">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
                <option value="6">6</option>
                <option value="7">7</option>
                <option value="8">8</option>
            </select>
            M: <select id="QR_MODEL" style="padding: 4px">
                <option value="1">Model 1</option>
                <option value="2" selected="">Model 2</option>
                <option value="3">Micro</option>
            </select>
        </fieldset>
        <br>
        <button class="green" style="width:100%;margin:0" onclick="printQrCode(); return false;">Print</button>
    </form>
<br>
</div>
</div>
<p><b>You can find additional information on the program website.</b></p>
<p style="text-align: center"><a style="font-size: 24px;color:green" href="https://rawbt.ru/">rawbt.ru</a></p>
<br>
<br>


<script src="./Test_files/app.js" charset="utf-8" async=""></script>


</body></html>