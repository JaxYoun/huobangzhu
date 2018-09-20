
module.exports = {
  name: 'troy',
  prefix: 'troy',
  footerText: 'Copyright(c) 2018 All rights reserved.版权所有-成都货帮主网络科技有限公司',
  footerPhone: '4008 888 888',
  logo: '/logo.png',
  logo2: '/logo2.png',
  iconFontCSS: '/iconfont.css',
  iconFontJS: '/iconfont.js',
  CORS: [],
  openPages: ['/', '/home', '/introduce', '/aboutUs'],
  apiPrefix: '/api',
  orderTypes: {
    BUY: '帮买',
    SEND: '帮送',
    FSL: '整车专线',
    LTL: '零担专线',
    EX: '快递',
    FAC: '找车',
    S: '积分订单',
  },
  orderState: {
    NEW: '新建',
    CONFIRMED: '已确认',
    ORDER_TO_BE_RECEIVED: '待接订单',
    LOCKED_ORDER: '锁定待确认',
    LOCKED_ORDER_DRIVER: '待司机确认',
    WAIT_TO_TAKE: '待取货',
    TRANSPORT: '取货运输中',
    WAIT_TO_CONFIRM: '收货待确认',
    WAIT_FOR_PAYMENT: '已确认待付款',
    PAID: '已付款',
    LIQUIDATION_COMPLETED: '已结算',
    OVER_TIME: '订单超时',
    WAITE_TO_REFUNDDE: '等待退款',
    APPLY_FOR_REFUND: '退款中',
    REFUND_FINISHT: '退款完成',
    INVALID: '作废',
  },
  collectStaut: {
    NEW: '未开始',
    CONFIRMED: '未开始',
    ORDER_TO_BE_RECEIVED: '征集中',
    LOCKED_ORDER: '征集中',
    WAIT_TO_TAKE: '征集完成',
    TRANSPORT: '征集完成',
    WAIT_TO_CONFIRM: '征集完成',
    WAIT_FOR_PAYMENT: '征集完成',
    PAID: '征集完成',
    LIQUIDATION_COMPLETED: '征集完成',
    OVER_TIME: '征集取消',
    APPLY_FOR_REFUND: '征集取消',
    WAITE_TO_REFUNDDE: '征集取消',
    REFUND_FINISHT: '征集取消',
    INVALID: '征集取消',
  },
  api: {
    userLogout: '/system/login/loginOut',
    userInfo: '/userInfo',
    users: '/users',
    user: '/system/login/getUserByUserIdToCache',
    menus: '/system/menu/list',
    switchPost: '/system/login/switchPost',
    queryPostList: '/system/post/queryPostList',

    queryUserList: '/user/query',
    enums: '/public/enums',                       // 枚举类型查询
    userLogin: '/security',
    userRoleMenu: '/web/user/getUserRoleMenu',    // 获取菜单列表
    telephoneCheck: '/user/app/telephone/check',
    authCodeSend: '/user/authCode/send',
    authCodePeek: '/user/authCode/peek',
    authCodeCheck: '/user/app/authCode/check',
    resetPwd: '/user/app/reset',
    registry: '/user/app/registry',
    token: '/session/token',
    addressQuery: '/user/app/address/query',
    addressDelete: '/user/app/address/delete',
    addressUpdate: '/user/app/address/update',
    addressCreate: '/user/app/address/create',
    typeval: '/public/typeval',
    geoConvert: '/map/geo/convert',
    LtlOrder: {
      create: '/web/ltlOrder/createLtlOrder',
      publish: '/web/ltlOrder/publishLtlOrder',
      unpublish: '/web/ltlOrder/unpublishLtlOrder',
      delete: '/web/ltlOrder/deleteLtlOrder',
      getOrderPage: '/web/ltlOrder/getLtlOrderByPage',
      detail: '/web/ltlOrder/getLtlOrderDetail',
      detailForUdate: '/web/ltlOrder/getLtlOrderDetailForUpdate',
      update: '/web/ltlOrder/updateLtlOrder',
      pay: '',
      downloadTemplate: '/web/ltlOrder/downloadLtlOrderTemplate',
      import: '/web/ltlOrder/importLtlOrder',
      getOrderDetail: '/order/get',  // 查看专线零担货源详情
      orderConfirm: '/order/ltl/confirm', // 订单确认
      query: '/order/ltl/query',
      countPrice: '/order/ltl/price',
    },
    fslOrder: {
      create: '/web/flsOrder/createFlsOrder',
      publish: '/web/flsOrder/publishFlsOrder',
      unpublish: '/web/flsOrder/unpublishFlsOrder',
      delete: '/web/flsOrder/deleteFlsOrder',
      getOrderPage: '/web/flsOrder/getFlsOrderByPage',
      detail: '/web/flsOrder/getFlsOrderDetail',
      detailForUdate: '/web/flsOrder/getFlsOrderDetailForUpdate',
      update: '/web/flsOrder/updateFlsOrder',
      pay: '',
      downloadTemplate: '/web/flsOrder/downloadFlsOrderTemplate',
      import: '/web/flsOrder/importFlsOrder',
      getOrderDetail: '/order/get', // 查看专线整车货源详情
      orderConfirm: '/order/fsl/confirm', // 订单确认
      query: '/order/fsl/query',
      get: '/order/fsl/get',
      tender: '/order/tender/get',
      consignor: '/order/taker/consignor/query',
      countPrice: '/order/fsl/price',
    },
    carCollect: {
      orderQuery: '/order/query',
      create: '/order/tender/create',
    },
    personalCenter: {
      getUserInfo: '/user/get',
      registry: '/user/app/registry/typed/registry',
      search: '/user/app/registry/search',
      enterpriseConsignorSubmit: '/user/app/registry/enterpriseConsignor/submit',
      transEnterpriseSubmit: '/user/app/registry/transEnterprise/submit',
      imageChange: '/user/updateHbzUser',
    },
    helpToSend: {
      create: '/web/proxyDelivery/createProxyDelivery',
      confirmProxyDelivery: '/web/proxyDelivery/queryProxyDeliveryDetail',
      computePrice: '/order/send/price/compute',
      confirmOrder: '/order/send/confirm',
    },
    helpToBuy: {
      create: '/web/proxyBuy/createProxyBuy',
      confirmProxyBuy: '/web/proxyBuy/getProxyBuyDetail',
      confirmOrder: '/order/buy/confirm',
    },
    carPublish: {
      transSize: '/transSize/all',
      createDriverLine: '/web/driverLine/createDriverLine',
    },
    accessories: {
      queryAddress: '/user/app/link/query',
      updateAddress: '/user/app/link/update',
      createAddress: '/user/app/link/create',
      deleteAddress: '/user/app/link/delete',
      priceCompute: '/order/ex/price/compute',
      exCreate: '/order/ex/create',
    },
    driverHelpToBuy: {
      nearQuery: '/order/buy/task/near/query',
      getDetail: '/order/get',
      buyCarry: '/order/buy/carry',
    },
    driverHelpToSend: {
      nearQuery: '/order/send/task/near/query',
      sendCarry: '/order/send/carry',
    },
    vehicleCollect: {
      queryAvailableTender: '/order/task/near/query',
      takerCreate: '/order/taker/create', // 司机参与征集
    },
    storageInformation: {
      getStorage: '/web/warehouse/getAvailableWarehouseListByPage',
      getStorageDetail: '/web/warehouse/getWarehouseDetail',
      queryBail: '/web/warehouse/getWarehouseEarnest', // 仓储租赁诚意金查询
      creatPayOrder: '/web/warehouse/generateWarehouseEarnestOrder', // 生成支付订单
      getBatchId: '/upload/queryFileByBatchId', // 获得图片id
    },
    receiveOrderList: {
      getReceiveList: '/order/task/queryPage', // 接运订单分页查询
      getLogistics: '/order/record/query', // 物流详情
      confirmSign: '/order/driverSignArrival', // 车主-送达签收
      fslTake: '/order/fsl/take', // 司机接运,
      ltlTake: '/order/ltl/take', // 零担司机接运
      buyTake: '/order/buy/take', // 帮我买取货
      sendTake: '/order/send/take', // 帮我送取货
      fslOk: '/order/fsl/complete', // 整车司机确认送到
      ltlOk: '/order/ltl/complete', // 零担司机确认送到
      buyOk: '/order/buy/complete', // 帮我买确认
      sendOk: '/order/send/complete', // 帮我送确认
      refuseUrl: 'order/refuse', // 拒绝签收
      fslDrivingAgree: '/order/fsl/driving/agree', // 月结整车同意
      ltlDrivingAgree: '/order/ltl/driving/agree',
      fslRefuseAgree: '/order/fsl/driving/refuse', // 月结整车拒绝签收
      ltlRefuseAgree: '/order/ltl/driving/refuse',
    },
    sendOrderList: {
      confirmSign: '/order/consignorConfirmArrival', // 货主-确认送达
      doFslAgree: '/order/fsl/agree', // 整车货主同意接运
      doLtlAgree: '/order/ltl/agree', // 零担货主同意接运
      fslSign: '/order/fsl/receive', // 整车确认已送到
      ltlSign: '/order/ltl/receive', // 零担货主确认已送到
      buySign: '/order/buy/receive', // 帮我买确认收货
      sendSign: '/order/send/receive', // 帮我送确认收货
    },
    storageManage: {
      save: '/web/warehouse/createWarehouse', // 创建仓储
      update: '/web/warehouse/updateWarehouse', // 修改仓储
      query: '/web/warehouse/getWarehouseDetail', // 查询仓储详情
    },
    appraiseOrder: {
      comment: '/rate/rateSign', // 发表评论
    },
    carInformation: {
      addCar: '/vehicleInformation/addVehicleInformation', // 新增车辆信息
      changeCar: '/vehicleInformation/updateVehicleInformation', // 修改车辆信息
    },
    companyInformation: {
      companyDetail: '/web/user/getCurrentUserEnterprise', // 查看公司信息
      updateCompanyInfo: '/enterprise/org/update', // 修改公司信息
    },
    accountManage: {
      enable: '/enterprise/user/enable', // 启用本企业用户,
      disable: '/enterprise/user/disable', // 停用本企业用户,
      queryRole: '/enterprise/user/availableRole', // 查询可用角色
      enterpriseOrg: '/enterprise/org/all', // 查询组织部门
      userAdd: '/enterprise/user/add', // 添加用户
      userUpdate: '/enterprise/user/update', // 编辑用户
      userDelete: '/enterprise/user/delete', // 删除用户
    },
    specialLineReceipt: {
      getFlsDetial: '/web/flsOrder/getFlsOrderDetail', // 查看整车货源详情
      getLtlDetial: '/web/ltlOrder/getLtlOrderDetail', // 查看零担货源详情
      takeFlsOrder: '/web/flsOrder/take', // 整车同意接单
      takeLtlOrder: '/web/ltlOrder/take', // 零担同意接单
    },
  },
  headMenu: [{
    id: '1',
    route: '/',
    icon: '',
    name: '网站首页',
  }, {
    id: '2',
    route: '/introduce',
    icon: '',
    limitLevel: '1',
    name: '业务介绍',
  }, {
    id: '3',
    route: '/consignorEnterprise',
    icon: '',
    limitLevel: '1',
    name: '我是货主',
  }, {
    id: '4',
    route: '/driverEnterprise',
    icon: '',
    limitLevel: '1',
    name: '我是司机',
  }, {
    id: '5',
    route: '/storageCenter/storageInformation',
    icon: '',
    limitLevel: '1',
    name: '仓储中心',
  }, {
    id: '6',
    route: '/personalCenter',
    icon: '',
    limitLevel: '1',
    name: '个人中心',
  }, {
    id: '7',
    route: '/aboutUs',
    icon: '',
    limitLevel: '1',
    name: '关于我们',
  }],
}
