/**
 * Created by leecheng on 2017/12/7.
 */
function log(a) {
    java.lang.System.out.println(a);
}
function sin(x) {
    return java.lang.Math.sin(x);
}
function cos(x) {
    return java.lang.Math.cos(x);
}
function tan(x) {
    return java.lang.Math.tan(x);
}
function exp(x) {
    return java.lang.Math.exp(x);
}
function exp(x) {
    return java.lang.Math.abs(x);
}
function ln(x) {
    return java.lang.Math.log(x) / java.lang.Math.log(java.lang.Math.E);
}
function power(a, x) {
    return java.lang.Math.pow(a, x);
}

var mathSupport = new com.troy.keeper.hbz.helper.MathSupport();

function notZero(a) {
    log('原始值:' + a);
    if (a == 0) return 0;
    return 1;
}

function isZero(a) {
    log('原始值:' + a);
    if (a == 0) return 1;
    return 0;
}

function minLtInt(a) {
    return mathSupport.minLtInt(a);
}

function mod(a, b) {
    log('原始值:' + a + ',' + b);
    return mathSupport.mod(a, b);
}

function intDiv(a, b) {
    log('原始值:' + a + ',' + b);
    return mathSupport.div(a, b);
}