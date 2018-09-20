import React from 'react'
import PropTypes from 'prop-types'
import { Router } from 'dva/router'
import App from './routes/app'
import troyAdmin from 'troy-admin'


let registerModel = (app, model) => {
  if (!(app._models.filter(m => m.namespace === model.namespace).length === 1)) {
    app.model(model)
  }
}

const Routers = function ({ history, app }) {
  let routes = [
    {
      path: '/',
      component: App,
      getIndexRoute (nextState, cb) {
        require.ensure([], require => {
          cb(null, { component: require('./routes/home/') })
        }, 'home')
      },
      childRoutes: [
        {
          path: '/home',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              cb(null, require('./routes/home/'))
            }, 'home')
          },
        }, {
          path: '/consignorEnterprise',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              cb(null, require('./routes/consignorEnterprise/'))
            }, 'consignorEnterprise')
          },
        }, {
          path: '/driverEnterprise',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              cb(null, require('./routes/driverEnterprise/'))
            }, 'driverEnterprise')
          },
        }, {
          path: '/storageCenter/storageInformation',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/storageInformation'))
              cb(null, require('./routes/storageCenter/storageInformation'))
            }, 'storageInformation')
          },
        }, {
          path: '/storageCenter/info',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/info'))
              cb(null, require('./routes/storageCenter/info'))
            }, 'info')
          },
        }, {
          path: '/storageCenter/storageInformation/storageDetail',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/storageDetail'))
              cb(null, require('./routes/storageCenter/storageInformation/storageDetail'))
            }, 'storageDetail')
          },
        }, {
          path: '/storageCenter/storageInformation/payBail',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/payBail'))
              cb(null, require('./routes/storageCenter/storageInformation/payBail'))
            }, 'payBail')
          },
        }, {
          path: '/storageCenter/pay',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/pay'))
              cb(null, require('./routes/storageCenter/pay'))
            }, 'pay')
          },
        }, {
          path: '/consignorEnterprise/pay',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/pay'))
              cb(null, require('./routes/consignorEnterprise/pay/'))
            }, 'pay')
          },
        }, {
          path: '/consignorEnterprise/specialLineTransport/fslOrder',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/consignorEnterprise/specialLineTransport'))
              cb(null, require('./routes/consignorEnterprise/specialLineTransport/fslOrder'))
            }, 'specialLineTransport')
          },
        }, {
          path: '/consignorEnterprise/specialLineTransport/LtlOrder',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/consignorEnterprise/specialLineTransport'))
              cb(null, require('./routes/consignorEnterprise/specialLineTransport/LtlOrder'))
            }, 'specialLineTransport')
          },
        }, {
          path: '/consignorEnterprise/specialLineTransport/importFsl',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/consignorEnterprise/specialLineTransport'))
              cb(null, require('./routes/consignorEnterprise/specialLineTransport/importFsl'))
            }, 'specialLineTransport')
          },
        }, {
          path: '/consignorEnterprise/specialLineTransport/importLtl',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/consignorEnterprise/specialLineTransport'))
              cb(null, require('./routes/consignorEnterprise/specialLineTransport/importLtl'))
            }, 'specialLineTransport')
          },
        }, {
          path: '/consignorEnterprise/specialLineTransport/confirmOrder',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/consignorEnterprise/specialLineTransport'))
              cb(null, require('./routes/consignorEnterprise/specialLineTransport/confirmOrder'))
            }, 'specialLineTransport')
          },
        }, {
          path: '/consignorEnterprise/specialLineTransport/selectedMonthlyPay',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/consignorEnterprise/specialLineTransport'))
              cb(null, require('./routes/consignorEnterprise/specialLineTransport/selectedMonthlyPay'))
            }, 'specialLineTransport')
          },
        }, {
          path: '/consignorEnterprise/specialLineTransport/onlinePay',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/consignorEnterprise/specialLineTransport'))
              cb(null, require('./routes/consignorEnterprise/specialLineTransport/onlinePay'))
            }, 'specialLineTransport')
          },
        }, {
          path: '/consignorEnterprise/carCollect',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/carCollect'))
              cb(null, require('./routes/consignorEnterprise/carCollect'))
            }, 'carCollect')
          },
        }, {
          path: '/consignorEnterprise/carCollect/carCollectTodo',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/carCollectTodo'))
              cb(null, require('./routes/consignorEnterprise/carCollect/carCollectTodo'))
            }, 'carCollectTodo')
          },
        }, {
          path: '/consignorEnterprise/carCollect/carCollectFinish',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/carCollectFinish'))
              cb(null, require('./routes/consignorEnterprise/carCollect/carCollectFinish'))
            }, 'carCollectFinish')
          },
        }, {
          path: '/consignorEnterprise/carCollect/carCollectDetail',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/carCollectDetail'))
              cb(null, require('./routes/consignorEnterprise/carCollect/carCollectDetail'))
            }, 'carCollectDetail')
          },
        }, {
          path: '/consignorEnterprise/carCollect/carCollectDoing',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/carCollectDoing'))
              cb(null, require('./routes/consignorEnterprise/carCollect/carCollectDoing'))
            }, 'carCollectDoing')
          },
        }, {
          path: '/consignorEnterprise/helpToSend',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/helpToSend'))
              cb(null, require('./routes/consignorEnterprise/helpToSend'))
            }, 'helpToSend')
          },
        }, {
          path: '/consignorEnterprise/helpToSend/helpToSendComfirm',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/helpToSendComfirm'))
              cb(null, require('./routes/consignorEnterprise/helpToSend/helpToSendComfirm'))
            }, 'helpToSendComfirm')
          },
        }, {
          path: '/driverEnterprise/publishDriverLine',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/publishDriverLine'))
              cb(null, require('./routes/driverEnterprise/publishDriverLine'))
            }, 'publishDriverLine')
          },
        }, {
          path: '/driverEnterprise/batchPublish',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/publishDriverLine'))
              cb(null, require('./routes/driverEnterprise/batchPublish'))
            }, 'publishDriverLine')
          },
        }, {
          path: '/driverEnterprise/driverHelpToBuy',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/driverHelpToBuy'))
              cb(null, require('./routes/driverEnterprise/driverHelpToBuy'))
            }, 'driverHelpToBuy')
          },
        }, {
          path: '/driverEnterprise/driverHelpToSend',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/driverHelpToSend'))
              cb(null, require('./routes/driverEnterprise/driverHelpToSend'))
            }, 'driverHelpToSend')
          },
        }, {
          path: '/driverEnterprise/vehicleCollect',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/vehicleCollect'))
              cb(null, require('./routes/driverEnterprise/vehicleCollect'))
            }, 'vehicleCollect')
          },
        }, {
          path: '/driverEnterprise/vehicleCollect/checkVehicleDetail',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/checkVehicleDetail'))
              cb(null, require('./routes/driverEnterprise/vehicleCollect/checkVehicleDetail'))
            }, 'checkVehicleDetail')
          },
        }, {
          path: '/driverEnterprise/driverHelpToBuy/confirmIsTakeCase',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/confirmIsTakeCase'))
              cb(null, require('./routes/driverEnterprise/driverHelpToBuy/confirmIsTakeCase'))
            }, 'confirmIsTakeCase')
          },
        }, {
          path: '/driverEnterprise/driverHelpToSend/confirmIsTakeSend',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/confirmIsTakeSend'))
              cb(null, require('./routes/driverEnterprise/driverHelpToSend/confirmIsTakeSend'))
            }, 'confirmIsTakeSend')
          },
        }, {
          path: '/driverEnterprise/goPay',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/pay'))
              cb(null, require('./routes/driverEnterprise/goPay'))
            }, 'goPay')
          },
        }, {
          path: '/driverEnterprise/specialLineReceipt/driverFslOrder',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/specialLineReceipt'))
              cb(null, require('./routes/driverEnterprise/specialLineReceipt/driverFslOrder'))
            }, 'specialLineReceipt')
          },
        }, {
          path: '/driverEnterprise/specialLineReceipt/driverLtlOrder',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/specialLineReceipt'))
              cb(null, require('./routes/driverEnterprise/specialLineReceipt/driverLtlOrder'))
            }, 'specialLineReceipt')
          },
        }, {
          path: '/driverEnterprise/specialLineReceipt/driverOrderDetail',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/driverOrderDetail'))
              cb(null, require('./routes/driverEnterprise/specialLineReceipt/driverOrderDetail'))
            }, 'driverOrderDetail')
          },
        }, {
          path: '/consignorEnterprise/helpToBuy',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/consignorEnterprise/helpToBuy'))
              cb(null, require('./routes/consignorEnterprise/helpToBuy'))
            }, 'helpToBuy')
          },
        }, {
          path: '/consignorEnterprise/helpToBuy/helpToBuyComfirm',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/consignorEnterprise/helpToBuyComfirm'))
              cb(null, require('./routes/consignorEnterprise/helpToBuy/helpToBuyComfirm'))
            }, 'helpToBuyComfirm')
          },
        }, {
          path: '/personalCenter',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/personalCenter'))
              cb(null, require('./routes/personalCenter/'))
            }, 'personalCenter')
          },
        }, {
          path: '/personalCenter/receiveOrderList',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/receiveOrderList'))
              cb(null, require('./routes/personalCenter/receiveOrderList'))
            }, 'receiveOrderList')
          },
        }, {
          path: '/personalCenter/receiveOrderList/receiveDetail',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/receiveDetail'))
              cb(null, require('./routes/personalCenter/receiveOrderList/receiveDetail'))
            }, 'receiveDetail')
          },
        }, {
          path: '/personalCenter/sendOrderList',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/sendOrderList'))
              cb(null, require('./routes/personalCenter/sendOrderList'))
            }, 'sendOrderList')
          },
        }, {
          path: '/personalCenter/personalInfo',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/personalInfo'))
              cb(null, require('./routes/personalCenter/personalInfo'))
            }, 'personalInfo')
          },
        }, {
          path: '/personalCenter/carInformation',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/carInformation'))
              cb(null, require('./routes/personalCenter/carInformation'))
            }, 'carInformation')
          },
        }, {
          path: '/personalCenter/myJoinOrder',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/myJoinOrder'))
              cb(null, require('./routes/personalCenter/myJoinOrder'))
            }, 'myJoinOrder')
          },
        }, {
          path: '/personalCenter/companyInformation',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/companyInformation'))
              cb(null, require('./routes/personalCenter/companyInformation'))
            }, 'companyInformation')
          },
        }, {
          path: '/personalCenter/accountManage',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/accountManage'))
              cb(null, require('./routes/personalCenter/accountManage'))
            }, 'accountManage')
          },
        }, {
          path: '/personalCenter/accountManage/accountDetail',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/accountManage'))
              cb(null, require('./routes/personalCenter/accountManage/accountDetail'))
            }, 'accountDetail')
          },
        }, {
          path: '/personalCenter/sendOrderList/sendDetail',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/sendDetail'))
              cb(null, require('./routes/personalCenter/sendOrderList/sendDetail'))
            }, 'sendDetail')
          },
        }, {
          path: '/personalCenter/authentication/enterpriseConsignor',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/personalCenter'))
              cb(null, require('./routes/personalCenter/authentication/enterpriseConsignor'))
            }, 'personalCenter')
          },
        }, {
          path: '/personalCenter/walletManage',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/walletManage'))
              cb(null, require('./routes/personalCenter/walletManage'))
            }, 'walletManage')
          },
        }, {
          path: '/personalCenter/walletManage/refundBond',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/refundBond'))
              cb(null, require('./routes/personalCenter/walletManage/refundBond'))
            }, 'refundBond')
          },
        }, {
          path: '/personalCenter/myStorageManage/myStorageEdit',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/myStorageEdit'))
              cb(null, require('./routes/personalCenter/myStorageManage/myStorageEdit'))
            }, 'myStorageEdit')
          },
        }, {
          path: '/personalCenter/myStorageManage/myStorageInfo',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/myStorageInfo'))
              cb(null, require('./routes/personalCenter/myStorageManage/myStorageInfo'))
            }, 'myStorageInfo')
          },
        }, {
          path: '/personalCenter/appraiseOrder/appraiseDriver',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/appraiseDriver'))
              cb(null, require('./routes/personalCenter/appraiseOrder/appraiseDriver'))
            }, 'appraiseDriver')
          },
        }, {
          path: '/personalCenter/appraiseOrder/appraiseOwner',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/appraiseOwner'))
              cb(null, require('./routes/personalCenter/appraiseOrder/appraiseOwner'))
            }, 'appraiseOwner')
          },
        }, {
          path: '/personalCenter/appraiseOrder/appraise',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/appraise'))
              cb(null, require('./routes/personalCenter/appraiseOrder/appraise'))
            }, 'appraise')
          },
        }, {
          path: '/personalCenter/authentication/transEnterprise',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/personalCenter'))
              cb(null, require('./routes/personalCenter/authentication/transEnterprise'))
            }, 'personalCenter')
          },
        },
        {
          path: '/consignorEnterprise/accessories',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/accessories'))
              cb(null, require('./routes/consignorEnterprise/accessories'))
            }, 'accessories')
          },
        }, {
          path: 'login',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/login'))
              cb(null, require('./routes/login/'))
            }, 'login')
          },
        }, {
          path: '*',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              cb(null, require('./routes/error/'))
            }, 'error')
          },
        },
      ],
    },
  ]
  // *****************加载路由 troy-admin *****************//
  for (let item of troyAdmin.routes.values()) {
    let childRoute = {
      path: `${troyAdmin.path}/${item}`,
      getComponent (nextState, cb) {
        require.ensure([], require => {
          registerModel(app, require(`troy-admin/lib/models/${item}.js`).default)
          cb(null, require(`troy-admin/lib/routes/${item}/index.js`).default)
        }, '')
      },
    }
    routes[0].childRoutes.splice(-1, 0, childRoute)
  }
  // *****************加载路由 troy-admin *****************//

  return <Router history={history} routes={routes} />
}

Routers.propTypes = {
  history: PropTypes.object,
  app: PropTypes.object,
}

export default Routers
