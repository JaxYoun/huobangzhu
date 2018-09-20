import { Injectable } from '@angular/core';
import {Http}from '@angular/http';
import { Storage } from '@ionic/storage';
import { HelperService } from '../../app/services/help.service';

declare var require;
declare var window;

@Injectable()
export class UserService {
    public enumsUrl: string = "/api/public/enums";//枚举类型
    private typevalUrl: string = "/api/public/typeval";//数据字典
    private queryMyRoleUrl: string = "/api/user/roles";//当前角色列表

    constructor(
        public helperService:HelperService,
        public storage: Storage,
        public http:Http
    ) {
        this.enumsUrl = this.helperService.api + this.enumsUrl;
        this.typevalUrl = this.helperService.api + this.typevalUrl;
        this.queryMyRoleUrl = this.helperService.api + this.queryMyRoleUrl;
    }

//保存用户信息到localstorage
 saveUserInfoToLocalStorage(myData,password) {
    //浏览器上推送，获取不到插件
    if(window.plugins){
        let alias = 'hbz_' + myData.data["X-AUTH-USER"];
        let params = { 'sequence': 1, 'alias': alias }
        window.plugins.jPushPlugin.setAlias(params,
          (data)=>{
            console.log(data)
            console.log('success')
          }, 
          (err)=>{
            console.log(err)
            console.log('fail')
          });
    }
    this.storage.set("loginName", myData.data["X-AUTH-USER"]);
    this.storage.set("password", password);
    this.storage.set("currentUser", myData.data["user"]);
    this.storage.set("currentUserComplexScore", myData.data["complexScore"]);//消费积分
    this.storage.set("errLoginCount", 0);
    this.storage.set("token", myData.data["X-AUTH-TOKEN"]).then(data => {
    this.helperService.getOptionss().then(options=>{
          //获取当前登录用户角色
        this.http.post(this.queryMyRoleUrl, {}, options)
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                this.storage.set("currentRoles", result.data);
              } else {
                this.helperService.presentToast('获取当前登录用户角色失败');
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
          //仓储订单状态：WarehouseEarnestPayStatusEnum
        this.http.post(this.enumsUrl, {"enumname":"WarehouseEarnestPayStatusEnum"}, options)
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                this.storage.set("warehouseEarnestPayStatusEnum", result.data);
              } else {
                this.helperService.presentToast('获取数据字典【仓储订单状态】失败');
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
          //仓储发布状态：warehouseAuditType
        this.http.post(this.typevalUrl, {"type":"WarehouseAuditType"}, options)
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                this.storage.set("warehouseAuditType", result.data);
              } else {
                this.helperService.presentToast('获取数据字典【仓储发布状态】失败');
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
          //订单：OrderType
        this.http.post(this.enumsUrl, {"enumname":"OrderType"}, options)
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                let orderTypeObj = result.data;
                // "BUY" : "帮买",
                // "SEND" : "帮送",
                // "FSL" : "整车专线",
                // "LTL" : "零担专线",
                // "EX" : "快递",
                // "FAC" : "找车",
                // "S" : "积分订单"
                delete orderTypeObj['FAC'];
                delete orderTypeObj['S'];
                this.storage.set("orderType", orderTypeObj);
              } else {
                this.helperService.presentToast('获取数据字典【订单类型】失败');
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
          //业务类型：BusinessType
        this.http.post(this.enumsUrl, {"enumname":"BusinessType"}, options)
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                this.storage.set("businessType", result.data);
              } else {
                this.helperService.presentToast('获取数据字典【业务类型】失败');
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
          //快递货物类型：CommodityClass
        this.http.post(this.enumsUrl, {"enumname":"CommodityClass"}, options)
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                this.storage.set("commodityClass", result.data);
              } else {
                this.helperService.presentToast('获取数据字典【货物类型】失败');
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
          //整车、零担货物类型：CommodityType
        this.http.post(this.enumsUrl, {"enumname":"CommodityType"}, options)
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                this.storage.set("commodityType", result.data);
              } else {
                this.helperService.presentToast('获取数据字典【货物类型】失败');
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
          //保存车辆类型：TransType
        this.http.post(this.enumsUrl, {"enumname":"TransType"}, options)
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                this.storage.set("transType", result.data);
              } else {
                this.helperService.presentToast('获取数据字典【车辆类型】失败');
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
        //保存体积单位：VolumeUnit
        this.http.post(this.enumsUrl, {"enumname":"VolumeUnit"}, options)
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                this.storage.set("volumeUnit", result.data);
              } else {
                this.helperService.presentToast('获取数据字典【体积单位】失败');
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
        //保存重量单位：WeightUnit
        this.http.post(this.enumsUrl, {"enumname":"WeightUnit"}, options)
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                this.storage.set("weightUnit", result.data);
              } else {
                this.helperService.presentToast('获取数据字典【重量单位】失败');
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
        //结算类型：SettlementType
        this.http.post(this.enumsUrl, {"enumname":"SettlementType"}, options)
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                this.storage.set("settlementType", result.data);
              } else {
                this.helperService.presentToast('获取数据字典【结算类型】失败');
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });
        //在线支付类型：PayType
        this.http.post(this.enumsUrl, {"enumname":"PayType"}, options)
          .subscribe((data)=> {
              let result = data.json();
              if (result.code == '200') {
                this.storage.set("payType", result.data);
              } else {
                this.helperService.presentToast('获取数据字典【在线支付类型】失败');
              }
            },
            (err)=> {
              this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
            });

        //货物重量
        this.http.post(this.typevalUrl,{type:'EX_WEIGHTEM'}, options).subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.storage.set("exWeightem", result.data);
          } else {
            this.helperService.presentToast('获取数据字典【货物重量】失败');
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
        //司机类型
        this.http.post(this.typevalUrl,{type:'Driver'}, options).subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.storage.set("driverType", result.data);
          } else {
            this.helperService.presentToast('获取数据字典【司机类型】失败');
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
        //注册资金要求
        this.http.post(this.typevalUrl,{type:'Registered_funds'}, options).subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.storage.set("registeredFunds", result.data);
          } else {
            this.helperService.presentToast('获取数据字典【注册资金要求】失败');
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
        //信誉等级
        this.http.post(this.typevalUrl,{type:'Credit_level'}, options).subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.storage.set("creditLevel", result.data);
          } else {
            this.helperService.presentToast('获取数据字典【信誉等级】失败');
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
        //保证金额度
        this.http.post(this.typevalUrl,{type:'Security_deposit'}, options).subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.storage.set("securityDeposit", result.data);
          } else {
            this.helperService.presentToast('获取数据字典【保证金额度】失败');
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
        //帮我买配送时间要求： TimeLimit
        this.http.post(this.enumsUrl, {"enumname":"TimeLimit"}, options)
        .subscribe((data)=> {
            let result = data.json();
            if (result.code == '200') {
              this.storage.set("timeLimit", result.data);
            } else {
              this.helperService.presentToast('获取数据字典【车辆类型】失败');
            }
          },
          (err)=> {
            this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
          });

          //帮我送取货时间时间要求：
        this.http.post(this.typevalUrl,{type:'PTIME_LIMIT'}, options).subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.storage.set("takeLimit", result.data);
          } else {
            this.helperService.presentToast('获取数据字典【司机类型】失败');
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
          //收款账号类型：
        this.http.post(this.enumsUrl,{enumname:'ReceiveAccountType'}, options).subscribe((data)=> {
          let result = data.json();
          if (result.code == '200') {
            this.storage.set("receiveAccountType", result.data);
          } else {
            this.helperService.presentToast('获取数据字典【收款账号类型】失败');
          }
        },
        (err)=> {
          this.helperService.presentToast('网络请求失败,请确保你的网络环境正常');
        });
      })
    });

      //读取本地地区JSON文件
      const areaJson = require('../../assets/areaJson.json');
      this.storage.set("areaJson", areaJson);
  }
}


