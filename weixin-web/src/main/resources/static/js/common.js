var AMAZE_UI = false;
/*var share_link = "html/share.html";
var share_title = "易加|靠谱的移动理财平台";
var share_content = "触手可及，财富易加";*/
var share_icon = "http://jupiter.static.jlfex.com/jupiter/v1.2.9.4/images/logo.png";
var share_title = "《叶问3》等精彩大片，易加请你看";
var share_link = "/html/bymovie.html";
var share_content = "领取电影兑换码，精彩影片请你看,关注易加，更有神秘好礼相送!";
var share_callback = true;
var share_callback_address = "/activity/starbucks";
//---------------------- 私有部分，不可直接访问 ------------------------
var _popup = null;
var _popupCallback = null;
var _PAGE_WIDTH = 320;
var _popupMap = {};
var _popupSeed = 0;
var _popupKey = "";
var _pageScale = 1;
//-------------------------sha1--------------------
/*sha1
 * A JavaScript implementation of the Secure Hash Algorithm, SHA-1, as defined
 * in FIPS PUB 180-1
 * Version 2.1a Copyright Paul Johnston 2000 - 2002.
 * Other contributors: Greg Holt, Andrew Kepert, Ydnar, Lostinet
 * Distributed under the BSD License
 * See http://pajhome.org.uk/crypt/md5 for details.
 */

/*
 * Configurable variables. You may need to tweak these to be compatible with
 * the server-side, but the defaults work in most cases.
 */
var hexcase = 0; /* hex output format. 0 - lowercase; 1 - uppercase        */
var b64pad = ""; /* base-64 pad character. "=" for strict RFC compliance   */
var chrsz = 8; /* bits per input character. 8 - ASCII; 16 - Unicode      */

/*
 * These are the functions you'll usually want to call
 * They take string arguments and return either hex or base-64 encoded strings
 */
function hex_sha1(s) {
    return binb2hex(core_sha1(str2binb(s), s.length * chrsz));
}

function b64_sha1(s) {
    return binb2b64(core_sha1(str2binb(s), s.length * chrsz));
}

function str_sha1(s) {
    return binb2str(core_sha1(str2binb(s), s.length * chrsz));
}

function hex_hmac_sha1(key, data) {
    return binb2hex(core_hmac_sha1(key, data));
}

function b64_hmac_sha1(key, data) {
    return binb2b64(core_hmac_sha1(key, data));
}

function str_hmac_sha1(key, data) {
    return binb2str(core_hmac_sha1(key, data));
}

/*
 * Perform a simple self-test to see if the VM is working
 */
function sha1_vm_test() {
    return hex_sha1("abc") == "a9993e364706816aba3e25717850c26c9cd0d89d";
}

/*
 * Calculate the SHA-1 of an array of big-endian words, and a bit length
 */
function core_sha1(x, len) {
    /* append padding */
    x[len >> 5] |= 0x80 << (24 - len % 32);
    x[((len + 64 >> 9) << 4) + 15] = len;

    var w = Array(80);
    var a = 1732584193;
    var b = -271733879;
    var c = -1732584194;
    var d = 271733878;
    var e = -1009589776;

    for (var i = 0; i < x.length; i += 16) {
        var olda = a;
        var oldb = b;
        var oldc = c;
        var oldd = d;
        var olde = e;

        for (var j = 0; j < 80; j++) {
            if (j < 16) w[j] = x[i + j];
            else w[j] = rol(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1);
            var t = safe_add(safe_add(rol(a, 5), sha1_ft(j, b, c, d)),
                safe_add(safe_add(e, w[j]), sha1_kt(j)));
            e = d;
            d = c;
            c = rol(b, 30);
            b = a;
            a = t;
        }

        a = safe_add(a, olda);
        b = safe_add(b, oldb);
        c = safe_add(c, oldc);
        d = safe_add(d, oldd);
        e = safe_add(e, olde);
    }
    return Array(a, b, c, d, e);

}

/*
 * Perform the appropriate triplet combination function for the current
 * iteration
 */
function sha1_ft(t, b, c, d) {
    if (t < 20) return (b & c) | ((~b) & d);
    if (t < 40) return b ^ c ^ d;
    if (t < 60) return (b & c) | (b & d) | (c & d);
    return b ^ c ^ d;
}

/*
 * Determine the appropriate additive constant for the current iteration
 */
function sha1_kt(t) {
    return (t < 20) ? 1518500249 : (t < 40) ? 1859775393 :
        (t < 60) ? -1894007588 : -899497514;
}

/*
 * Calculate the HMAC-SHA1 of a key and some data
 */
function core_hmac_sha1(key, data) {
    var bkey = str2binb(key);
    if (bkey.length > 16) bkey = core_sha1(bkey, key.length * chrsz);

    var ipad = Array(16),
        opad = Array(16);
    for (var i = 0; i < 16; i++) {
        ipad[i] = bkey[i] ^ 0x36363636;
        opad[i] = bkey[i] ^ 0x5C5C5C5C;
    }

    var hash = core_sha1(ipad.concat(str2binb(data)), 512 + data.length * chrsz);
    return core_sha1(opad.concat(hash), 512 + 160);
}

/*
 * Add integers, wrapping at 2^32. This uses 16-bit operations internally
 * to work around bugs in some JS interpreters.
 */
function safe_add(x, y) {
    var lsw = (x & 0xFFFF) + (y & 0xFFFF);
    var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
    return (msw << 16) | (lsw & 0xFFFF);
}

/*
 * Bitwise rotate a 32-bit number to the left.
 */
function rol(num, cnt) {
    return (num << cnt) | (num >>> (32 - cnt));
}

/*
 * Convert an 8-bit or 16-bit string to an array of big-endian words
 * In 8-bit function, characters >255 have their hi-byte silently ignored.
 */
function str2binb(str) {
    var bin = Array();
    var mask = (1 << chrsz) - 1;
    for (var i = 0; i < str.length * chrsz; i += chrsz)
        bin[i >> 5] |= (str.charCodeAt(i / chrsz) & mask) << (32 - chrsz - i % 32);
    return bin;
}

/*
 * Convert an array of big-endian words to a string
 */
function binb2str(bin) {
    var str = "";
    var mask = (1 << chrsz) - 1;
    for (var i = 0; i < bin.length * 32; i += chrsz)
        str += String.fromCharCode((bin[i >> 5] >>> (32 - chrsz - i % 32)) & mask);
    return str;
}

/*
 * Convert an array of big-endian words to a hex string.
 */
function binb2hex(binarray) {
    var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
    var str = "";
    for (var i = 0; i < binarray.length * 4; i++) {
        str += hex_tab.charAt((binarray[i >> 2] >> ((3 - i % 4) * 8 + 4)) & 0xF) +
            hex_tab.charAt((binarray[i >> 2] >> ((3 - i % 4) * 8)) & 0xF);
    }
    return str;
}

/*
 * Convert an array of big-endian words to a base-64 string
 */
function binb2b64(binarray) {
    var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    var str = "";
    for (var i = 0; i < binarray.length * 4; i += 3) {
        var triplet = (((binarray[i >> 2] >> 8 * (3 - i % 4)) & 0xFF) << 16) | (((binarray[i + 1 >> 2] >> 8 * (3 - (i + 1) % 4)) & 0xFF) << 8) | ((binarray[i + 2 >> 2] >> 8 * (3 - (i + 2) % 4)) & 0xFF);
        for (var j = 0; j < 4; j++) {
            if (i * 8 + j * 6 > binarray.length * 32) str += b64pad;
            else str += tab.charAt((triplet >> 6 * (3 - j)) & 0x3F);
        }
    }
    return str;
}



/*md5
 * A JavaScript implementation of the RSA Data Security, Inc. MD5 Message
 * Digest Algorithm, as defined in RFC 1321.
 * Version 2.1 Copyright (C) Paul Johnston 1999 - 2002.
 * Other contributors: Greg Holt, Andrew Kepert, Ydnar, Lostinet
 * Distributed under the BSD License
 * See http://pajhome.org.uk/crypt/md5 for more info.
 */

/*
 * Configurable variables. You may need to tweak these to be compatible with
 * the server-side, but the defaults work in most cases.
 */
var hexcase = 0; /* hex output format. 0 - lowercase; 1 - uppercase        */
var b64pad = ""; /* base-64 pad character. "=" for strict RFC compliance   */
var chrsz = 8; /* bits per input character. 8 - ASCII; 16 - Unicode      */

/*
 * These are the functions you'll usually want to call
 * They take string arguments and return either hex or base-64 encoded strings
 */
function hex_md5(s) {
    return binl2hex(core_md5(str2binl(s), s.length * chrsz));
}

function b64_md5(s) {
    return binl2b64(core_md5(str2binl(s), s.length * chrsz));
}

function str_md5(s) {
    return binl2str(core_md5(str2binl(s), s.length * chrsz));
}

function hex_hmac_md5(key, data) {
    return binl2hex(core_hmac_md5(key, data));
}

function b64_hmac_md5(key, data) {
    return binl2b64(core_hmac_md5(key, data));
}

function str_hmac_md5(key, data) {
    return binl2str(core_hmac_md5(key, data));
}

/*
 * Perform a simple self-test to see if the VM is working
 */
function md5_vm_test() {
    return hex_md5("abc") == "900150983cd24fb0d6963f7d28e17f72";
}

/*
 * Calculate the MD5 of an array of little-endian words, and a bit length
 */
function core_md5(x, len) {
    /* append padding */
    x[len >> 5] |= 0x80 << ((len) % 32);
    x[(((len + 64) >>> 9) << 4) + 14] = len;

    var a = 1732584193;
    var b = -271733879;
    var c = -1732584194;
    var d = 271733878;

    for (var i = 0; i < x.length; i += 16) {
        var olda = a;
        var oldb = b;
        var oldc = c;
        var oldd = d;

        a = md5_ff(a, b, c, d, x[i + 0], 7, -680876936);
        d = md5_ff(d, a, b, c, x[i + 1], 12, -389564586);
        c = md5_ff(c, d, a, b, x[i + 2], 17, 606105819);
        b = md5_ff(b, c, d, a, x[i + 3], 22, -1044525330);
        a = md5_ff(a, b, c, d, x[i + 4], 7, -176418897);
        d = md5_ff(d, a, b, c, x[i + 5], 12, 1200080426);
        c = md5_ff(c, d, a, b, x[i + 6], 17, -1473231341);
        b = md5_ff(b, c, d, a, x[i + 7], 22, -45705983);
        a = md5_ff(a, b, c, d, x[i + 8], 7, 1770035416);
        d = md5_ff(d, a, b, c, x[i + 9], 12, -1958414417);
        c = md5_ff(c, d, a, b, x[i + 10], 17, -42063);
        b = md5_ff(b, c, d, a, x[i + 11], 22, -1990404162);
        a = md5_ff(a, b, c, d, x[i + 12], 7, 1804603682);
        d = md5_ff(d, a, b, c, x[i + 13], 12, -40341101);
        c = md5_ff(c, d, a, b, x[i + 14], 17, -1502002290);
        b = md5_ff(b, c, d, a, x[i + 15], 22, 1236535329);

        a = md5_gg(a, b, c, d, x[i + 1], 5, -165796510);
        d = md5_gg(d, a, b, c, x[i + 6], 9, -1069501632);
        c = md5_gg(c, d, a, b, x[i + 11], 14, 643717713);
        b = md5_gg(b, c, d, a, x[i + 0], 20, -373897302);
        a = md5_gg(a, b, c, d, x[i + 5], 5, -701558691);
        d = md5_gg(d, a, b, c, x[i + 10], 9, 38016083);
        c = md5_gg(c, d, a, b, x[i + 15], 14, -660478335);
        b = md5_gg(b, c, d, a, x[i + 4], 20, -405537848);
        a = md5_gg(a, b, c, d, x[i + 9], 5, 568446438);
        d = md5_gg(d, a, b, c, x[i + 14], 9, -1019803690);
        c = md5_gg(c, d, a, b, x[i + 3], 14, -187363961);
        b = md5_gg(b, c, d, a, x[i + 8], 20, 1163531501);
        a = md5_gg(a, b, c, d, x[i + 13], 5, -1444681467);
        d = md5_gg(d, a, b, c, x[i + 2], 9, -51403784);
        c = md5_gg(c, d, a, b, x[i + 7], 14, 1735328473);
        b = md5_gg(b, c, d, a, x[i + 12], 20, -1926607734);

        a = md5_hh(a, b, c, d, x[i + 5], 4, -378558);
        d = md5_hh(d, a, b, c, x[i + 8], 11, -2022574463);
        c = md5_hh(c, d, a, b, x[i + 11], 16, 1839030562);
        b = md5_hh(b, c, d, a, x[i + 14], 23, -35309556);
        a = md5_hh(a, b, c, d, x[i + 1], 4, -1530992060);
        d = md5_hh(d, a, b, c, x[i + 4], 11, 1272893353);
        c = md5_hh(c, d, a, b, x[i + 7], 16, -155497632);
        b = md5_hh(b, c, d, a, x[i + 10], 23, -1094730640);
        a = md5_hh(a, b, c, d, x[i + 13], 4, 681279174);
        d = md5_hh(d, a, b, c, x[i + 0], 11, -358537222);
        c = md5_hh(c, d, a, b, x[i + 3], 16, -722521979);
        b = md5_hh(b, c, d, a, x[i + 6], 23, 76029189);
        a = md5_hh(a, b, c, d, x[i + 9], 4, -640364487);
        d = md5_hh(d, a, b, c, x[i + 12], 11, -421815835);
        c = md5_hh(c, d, a, b, x[i + 15], 16, 530742520);
        b = md5_hh(b, c, d, a, x[i + 2], 23, -995338651);

        a = md5_ii(a, b, c, d, x[i + 0], 6, -198630844);
        d = md5_ii(d, a, b, c, x[i + 7], 10, 1126891415);
        c = md5_ii(c, d, a, b, x[i + 14], 15, -1416354905);
        b = md5_ii(b, c, d, a, x[i + 5], 21, -57434055);
        a = md5_ii(a, b, c, d, x[i + 12], 6, 1700485571);
        d = md5_ii(d, a, b, c, x[i + 3], 10, -1894986606);
        c = md5_ii(c, d, a, b, x[i + 10], 15, -1051523);
        b = md5_ii(b, c, d, a, x[i + 1], 21, -2054922799);
        a = md5_ii(a, b, c, d, x[i + 8], 6, 1873313359);
        d = md5_ii(d, a, b, c, x[i + 15], 10, -30611744);
        c = md5_ii(c, d, a, b, x[i + 6], 15, -1560198380);
        b = md5_ii(b, c, d, a, x[i + 13], 21, 1309151649);
        a = md5_ii(a, b, c, d, x[i + 4], 6, -145523070);
        d = md5_ii(d, a, b, c, x[i + 11], 10, -1120210379);
        c = md5_ii(c, d, a, b, x[i + 2], 15, 718787259);
        b = md5_ii(b, c, d, a, x[i + 9], 21, -343485551);

        a = safe_add(a, olda);
        b = safe_add(b, oldb);
        c = safe_add(c, oldc);
        d = safe_add(d, oldd);
    }
    return Array(a, b, c, d);

}

/*
 * These functions implement the four basic operations the algorithm uses.
 */
function md5_cmn(q, a, b, x, s, t) {
    return safe_add(bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s), b);
}

function md5_ff(a, b, c, d, x, s, t) {
    return md5_cmn((b & c) | ((~b) & d), a, b, x, s, t);
}

function md5_gg(a, b, c, d, x, s, t) {
    return md5_cmn((b & d) | (c & (~d)), a, b, x, s, t);
}

function md5_hh(a, b, c, d, x, s, t) {
    return md5_cmn(b ^ c ^ d, a, b, x, s, t);
}

function md5_ii(a, b, c, d, x, s, t) {
    return md5_cmn(c ^ (b | (~d)), a, b, x, s, t);
}

/*
 * Calculate the HMAC-MD5, of a key and some data
 */
function core_hmac_md5(key, data) {
    var bkey = str2binl(key);
    if (bkey.length > 16) bkey = core_md5(bkey, key.length * chrsz);

    var ipad = Array(16),
        opad = Array(16);
    for (var i = 0; i < 16; i++) {
        ipad[i] = bkey[i] ^ 0x36363636;
        opad[i] = bkey[i] ^ 0x5C5C5C5C;
    }

    var hash = core_md5(ipad.concat(str2binl(data)), 512 + data.length * chrsz);
    return core_md5(opad.concat(hash), 512 + 128);
}

/*
 * Add integers, wrapping at 2^32. This uses 16-bit operations internally
 * to work around bugs in some JS interpreters.
 */
function safe_add(x, y) {
    var lsw = (x & 0xFFFF) + (y & 0xFFFF);
    var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
    return (msw << 16) | (lsw & 0xFFFF);
}

/*
 * Bitwise rotate a 32-bit number to the left.
 */
function bit_rol(num, cnt) {
    return (num << cnt) | (num >>> (32 - cnt));
}

/*
 * Convert a string to an array of little-endian words
 * If chrsz is ASCII, characters >255 have their hi-byte silently ignored.
 */
function str2binl(str) {
    var bin = Array();
    var mask = (1 << chrsz) - 1;
    for (var i = 0; i < str.length * chrsz; i += chrsz)
        bin[i >> 5] |= (str.charCodeAt(i / chrsz) & mask) << (i % 32);
    return bin;
}

/*
 * Convert an array of little-endian words to a string
 */
function binl2str(bin) {
    var str = "";
    var mask = (1 << chrsz) - 1;
    for (var i = 0; i < bin.length * 32; i += chrsz)
        str += String.fromCharCode((bin[i >> 5] >>> (i % 32)) & mask);
    return str;
}

/*
 * Convert an array of little-endian words to a hex string.
 */
function binl2hex(binarray) {
    var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
    var str = "";
    for (var i = 0; i < binarray.length * 4; i++) {
        str += hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8 + 4)) & 0xF) +
            hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8)) & 0xF);
    }
    return str;
}

/*
 * Convert an array of little-endian words to a base-64 string
 */
function binl2b64(binarray) {
    var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    var str = "";
    for (var i = 0; i < binarray.length * 4; i += 3) {
        var triplet = (((binarray[i >> 2] >> 8 * (i % 4)) & 0xFF) << 16) | (((binarray[i + 1 >> 2] >> 8 * ((i + 1) % 4)) & 0xFF) << 8) | ((binarray[i + 2 >> 2] >> 8 * ((i + 2) % 4)) & 0xFF);
        for (var j = 0; j < 4; j++) {
            if (i * 8 + j * 6 > binarray.length * 32) str += b64pad;
            else str += tab.charAt((triplet >> 6 * (3 - j)) & 0x3F);
        }
    }
    return str;
}



//-------------------------



function _initialize() {
    if (!AMAZE_UI) {
        _fixScreenWidth();
    }
    // $("a").attr("href", "javascript:void(0)");
    $("input").each(function() {
        var obj = $(this);
        if (obj.attr("noc") == "true") {
            return;
        }
        if (obj.attr("type") == "hidden" || obj.attr("type") == "checkbox") {
            return;
        }
        var close = $('<a href="javascript:void(0)" class="input_clean"></a>');
        obj.parent().append(close);
        close.click(function() {
            obj.val("");
        });
        close.hide();
        obj.keydown(function() {
            setTimeout(function() {
                if (obj.val() == "") {
                    close.hide();
                } else {
                    close.show();
                }
            }, 100)
        });
    });
}

function _createDialog(title, content, ok, yes, no, callback) {
    _popupCallback = callback;
    _popup = $("<div id='popup'></div>");
    var html = '<div id="dialog">';
    html += '<div id="dialog_text">';
    html += '<div id="dialog_text_title">' + title + '</div>';
    html += '<div id="dialog_text_content">' + content + '</div>';
    html += '</div>';
    html += '<div id="dialog_options">';
    if (ok) {
        html += ' <a href="javascript:void(0)" onclick="_onClickDialogClose(0)" id="dialog_ok">' + ok + '</a>';
    } else {
        html += '<a href="javascript:void(0)"  onclick="_onClickDialogClose(0)" id="dialog_no">' + no + '</a>';
        html += '<span></span>';
        html += '<a href="javascript:void(0)"  onclick="_onClickDialogClose(1)" id="dialog_yes">' + yes + '</a>';
    }
    html += '</div>';
    html += '</div>';
    _popup.html(html);
    $("body").append(_popup);
}

function _onClickDialogClose(type) {
    if (_popup) {
        _popup.remove();
        _popup = null;
    }
    if (_popupCallback) {
        var callback = _popupCallback;
        _popupCallback = null;
        callback(type);
    }
}

function _fixScreenWidth() {
    var device = document.documentElement.clientWidth;
    var ratio = device / _PAGE_WIDTH;
    if (ratio != 1) {
        document.body.style.zoom = ratio;
        _pageScale = ratio;
    }
}

function _startInitWX() {
	var startFix = "";
    post("/getjsapiTicket", {}, "json", function(data) {
    	console.info(data);
        var appId = data.appId;
        var jsapi_ticket = data.jsapi_ticket;
        var nonceStr = "hello";
        var timestamp = new Date().getTime();
        var url = window.location.href;
        var index = url.lastIndexOf("/");
        startFix = data.appUrl;
        var content = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
        var signature = hex_sha1(content);
        //var link = "http://wap.jlfex.com/html/share.html";
        var link = startFix + share_link;
        var title = share_title;
        var content = share_content;
        var icon = share_icon;
        //alert(icon);
        wx.config({
            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: appId, // 必填，公众号的唯一标识
            timestamp: timestamp, // 必填，生成签名的时间戳
            nonceStr: nonceStr, // 必填，生成签名的随机串
            signature: signature, // 必填，签名，见附录1
            jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage','previewImage','chooseImage','uploadImage'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
        wx.ready(function() {
            wx.onMenuShareTimeline({//分享到朋友圈
                title: title, // 分享标题
                link: link, // 分享链接
                imgUrl: icon, // 分享图标
                success: function() {
                	if(isNotNull($('.mask')) && isNotNull($('#helpshare'))){
                		shareEnd();
                	}
                	
                	if(share_callback){
                		load(startFix+share_callback_address);
                	}
                },
                cancel: function() {
                    // 用户取消分享后执行的回调函数
                }
            });
            wx.onMenuShareAppMessage({ //分享给朋友
                title: title, // 分享标题
                desc: content, // 分享描述
                link: link, // 分享链接
                imgUrl: icon, // 分享图标
                type: 'link', // 分享类型,music、video或link，不填默认为link
                dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                success: function() {
                	//分享成功--->隐藏遮罩层
                	if(isNotNull($('.mask')) && isNotNull($('#helpshare'))){
                		shareEnd();
                	}
                	
                	if(share_callback){
                	    load(startFix+share_callback_address);
                	}
                },
                cancel: function() {
                    // 用户取消分享后执行的回调函数
                }
            });
        });
    });
}

// ---------------------- 公共部分 ------------------------
function getOriginWidth(value) {
    return parseInt(value / _pageScale);
}

/**
 * 显示提示框
 */
function showAlert(content, callback, title, ok) {
    if (title == null) {
        title = "提示";
    }
    if (ok == null) {
        ok = "确定";
    }
    if (AMAZE_UI) {
        var alertObj = $("#common_alert");
        if (alertObj.length == 0) {
            alertObj = $('<div class="am-modal am-modal-confirm" tabindex="-1" id="common_alert"></div>');
            var html = '<div class="shade-box am-modal-dialog">';
            html += '<p class="ftc-style" id="common_alert_title">' + title + '</p>';
            html += '<p class="color666" id="common_alert_content">' + content + '</p>';
            html += '<div class="btn-team am-modal-footer">';
            html += '<a class="am-modal-btn" id="common_alert_ok" data-am-modal-confirm>' + ok + '</a>';
            html += '</div></div>';
            alertObj.html(html);
            $("body").append(alertObj);
            alertObj.modal({
                "closeViaDimmer": false,
                "onConfirm": function(e) {
                    if (callback) {
                        callback(1);
                    }
                }
            });
        } else {
            $("#common_alert_title").html(title);
            $("#common_alert_content").html(content);
            $("#common_alert_ok").html(ok);
        }
        alertObj.modal('open');
    } else {
        _popupCallback = callback;
        var popone = $("<div id='popup'></div>");
        var html = '<div class="dialog0">';
        html += '<a class="pay_close" href="javascript:void(0)" onclick="_onClickAlertClose(-1)"></a>';
        html += '<div class="dialog_content">' + content + '</div>';
        html += '<a class="dialog_btn" href="javascript:void(0)" onclick="_onClickAlertClose(0)">' + ok + '</a>';
        html += '</div>';
        popone.html(html);
        $("body").append(popone);
        _popupSeed++;
        _popupKey = "alert_" + _popupSeed;
        _popupMap[_popupKey] = popone;
    }
}

function _onClickAlertClose(type) {
    for (var key in _popupMap) {
        _popupMap[key].remove();
    }
    _popupMap = {};

    if (_popupCallback) {
        var callback = _popupCallback;
        _popupCallback = null;
        callback(type);
    }
}

/**
 * 显示确认框
 */
function showConfirm(content, callback, title, yes, no) {
    if (title == null) {
        title = "提示";
    }
    if (yes == null) {
        yes = "是";
    }
    if (no == null) {
        no = "否";
    }
    if (AMAZE_UI) {
        var alertObj = $("#common_confirm");
        if (alertObj.length == 0) {
            alertObj = $('<div class="am-modal am-modal-confirm" tabindex="-1" id="common_confirm"></div>');
            var html = '<div class="shade-box am-modal-dialog">';
            html += '<p class="amargin-top22 am-text-default color333" id="common_confirm_title">' + title + '</p>';
            html += '<p class="color666" id="common_confirm_content">' + content + '</p>';
            html += '<div class="btn-team am-modal-footer">';
            html += '<a class="am-modal-btn color5392ef" id="common_confirm_no" data-am-modal-cancel>' + no + '</a>';
            html += '<a class="am-modal-btn colorf6742b" id="common_confirm_yes" data-am-modal-confirm>' + yes + '</a>';
            html += '</div></div>';
            alertObj.html(html);
            $("body").append(alertObj);
            alertObj.modal({
                "closeViaDimmer": false,
                "onConfirm": function(e) {
                    if (callback) {
                        callback(1);
                    }
                },
                "onCancel": function(e) {
                    if (callback) {
                        callback(0);
                    }
                }
            });
        } else {
            $("#common_confirm_title").html(title);
            $("#common_confirm_content").html(content);
            $("#common_confirm_yes").html(yes);
            $("#common_confirm_no").html(no);
        }
        alertObj.modal('open');
    } else {
        _createDialog(title, content, null, yes, no, callback);
    }
}

function post(api, params, dataType, callback, error) {
    var timestamp = Date.parse(new Date());
    api = api + "?timestamp=" + timestamp;
    $.ajax({
        url: api,
        dataType: dataType,
        method: 'post',
        data: params,
        success: callback,
        error: error
    });
}

function load(api) {
    window.location = api;
    /*
     * $.ajax({ url: api, success: function(result) { $('body').html(result); }
     * });
     */
}


//对象空值判断
function isNotNull(obj){
	if(obj == "" || obj == null || obj == undefined){
		return false;
	}else{
		return true;
	}
}

function toHome() {
    load("/home");
}

function toAccount() {
    load("/myAccount");
}

function toMore() {
	load("/activity/movie/more");
}

$(function() {
    _initialize();
    _startInitWX();
});

