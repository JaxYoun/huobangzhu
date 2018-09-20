export class Config {
    public GPS_SYNCHRO_TIME = "60000";  //GPS地理位置同步间隔
    public scrollWaitTime = "1000";  //下拉滚动条刷新，加载数据等待时间2秒
    public pageSize = 10;  //每页显示数量


    // public API = "http://10.0.203.1:80"; //生产环境
    // public masterSecret = "009831f99ae5f52efeafa448";  // 生产环境-极光推送密钥
    // public appKey = "b2595f90dc08e8a6ed43e1ea";  // 生产环境-极光推送appKey
    // public uploadPre = "http://10.0.203.6:80/static/upload/";  // 生产环境-APP附件上传目录，整车、零担使用的


    public API = "http://218.88.13.37:8082"; //外网、演示
    // public API = "http://10.0.40.74"; //测试
    // public API = "http://10.0.40.112";

    public masterSecret = "2a2e407caf545dc3d90b8a71";  // 开发、演示-极光推送密钥
    public appKey = "06b469dabeb5f2bd8b6aeacc";  //  开发、演示-极光推送appKey
    // public uploadPre = "http://10.0.40.113:9999/static/upload/";  // 开发-APP附件上传目录，整车、零担使用的
    public uploadPre = "http://218.88.13.37:9090/static/upload/";  // 演示-APP附件上传目录，整车、零担使用的

    constructor(){}
}
