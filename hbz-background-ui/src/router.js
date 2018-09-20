import React from 'react'
import PropTypes from 'prop-types'
import { Router } from 'dva/router'
import App from './routes/app'

let registerModel = (app, model) => {
  if (
    !(app._models.filter(m => m.namespace === model.namespace).length === 1)
  ) {
    app.model(model)
  }
}

const Routers = function ({ history, app }) {
  let routes = [
    {
      path: '/',
      component: App,
      getIndexRoute (nextState, cb) {
        require.ensure(
          [],
          require => {
            cb(null, { component: require('./routes/index/') })
          },
          'index'
        )
      },
      childRoutes: [
        {
          path: 'login',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/login'))
                cb(null, require('./routes/login/'))
              },
              'login'
            )
          },
        },
        {
          path: 'system/org',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/org'))
                cb(null, require('./routes/system/org'))
              },
              'org'
            )
          },
        },
        {
          path: 'system/limit',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/limit'))
                cb(null, require('./routes/system/limit'))
              },
              'limit'
            )
          },
        },
        {
          path: 'system/menu',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/menu'))
                cb(null, require('./routes/system/menu'))
              },
              'menu'
            )
          },
        },
        {
          path: 'system/post',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/post'))
                cb(null, require('./routes/system/post'))
              },
              'post'
            )
          },
        },
        {},
        {
          path: 'system/batchpost',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/batchpost'))
                cb(null, require('./routes/system/batchpost'))
              },
              'batchpost'
            )
          },
        },
        {
          path: 'system/role',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/role'))
                cb(null, require('./routes/system/role'))
              },
              'role'
            )
          },
        },
        {
          path: 'orderManagement/specialLine',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/specialLine'))
                cb(null, require('./routes/orderManagement/specialLine'))
              },
              'specialLine'
            )
          },
        },
        {
          path: 'orderManagement/cityDistribution',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/cityDistribution'))
                cb(null, require('./routes/orderManagement/cityDistribution'))
              },
              'cityDistribution'
            )
          },
        },
        {
          path: 'orderManagement/expressOrder',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/expressOrder'))
                cb(null, require('./routes/orderManagement/expressOrder'))
              },
              'expressOrder'
            )
          },
        },
        {
          path: 'integralShop/basicData',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/basicData'))
                cb(null, require('./routes/integralShop/basicData'))
              },
              'basicData'
            )
          },
        },
        {
          path: 'integralShop/scoreExchange',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/scoreExchange'))
                cb(null, require('./routes/integralShop/scoreExchange'))
              },
              'scoreExchange'
            )
          },
        },
        {
          path: 'logistics/basicData/userManagement',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/userManagement'))
              cb(null, require('./routes/logistics/basicData/userManagement'))
            }, 'userManagement')
          },
        },
        {
          path: 'logistics/basicData/carManagement',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/carManagement'))
              cb(null, require('./routes/logistics/basicData/carManagement'))
            }, 'carManagement')
          },
        },
        {
          path: 'logistics/basicData/goodsManagement',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/goodsManagement'))
              cb(null, require('./routes/logistics/basicData/goodsManagement'))
            }, 'goodsManagement')
          },
        },
        {
          path: 'userMessage/userExamine',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/userExamine'))
                cb(null, require('./routes/userMessage/userExamine'))
              },
              'userExamine'
            )
          },
        },
        {
          path: 'userMessage/urlManagement',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/urlManagement'))
                cb(null, require('./routes/userMessage/urlManagement'))
              },
              'urlManagement'
            )
          },
        },
        {
          path: 'userMessage/authorityManagement',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/authorityManagement'))
                cb(null, require('./routes/userMessage/authorityManagement'))
              },
              'userExamine'
            )
          },
        },
        {
          path: 'userMessage/guestManagement',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/guestManagement'))
                cb(null, require('./routes/userMessage/guestManagement'))
              },
              'guestManagement'
            )
          },
        },
        {
          path: 'userMessage/roleManagement',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/roleManagement'))
                cb(null, require('./routes/userMessage/roleManagement'))
              },
              'roleManagement'
            )
          },
        },
        {
          path: 'userMessage/menuManagement',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/menuManagement'))
                cb(null, require('./routes/userMessage/menuManagement'))
              },
              'menuManagement'
            )
          },
        },
        {
          path: 'userMessage/ruleManage/ruleConfigure',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/ruleManage'))
              cb(null, require('./routes/userMessage/ruleManage/ruleConfigure'))
            }, 'ruleManage')
          },
        }, {
          path: 'userMessage/ruleManage/integralQuery',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/ruleManage'))
              cb(null, require('./routes/userMessage/ruleManage/integralQuery'))
            }, 'ruleManage')
          },
        }, {
          path: 'userMessage/ruleManage/reputationQuery',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/ruleManage'))
              cb(null, require('./routes/userMessage/ruleManage/reputationQuery'))
            }, 'ruleManage')
          },
        }, {
          path: 'msgManage/insideMsg',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/insideMsg'))
              cb(null, require('./routes/msgManage/insideMsg'))
            }, 'insideMsg')
          },
        }, {
          path: 'msgManage/sentMsg',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/sentMsg'))
              cb(null, require('./routes/msgManage/sentMsg'))
            }, 'sentMsg')
          },
        }, {
          path: 'webManagement/informationManagement',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/informationManagement'))
                cb(
                  null,
                  require('./routes/webManagement/informationManagement')
                )
              },
              'informationManagement'
            )
          },
        },
        {
          path: 'storageManagement/storageExamine',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/storageExamine'))
                cb(null, require('./routes/storageManagement/storageExamine'))
              },
              'storageExamine'
            )
          },
        },
        {
          path: 'storageManagement/storageInfo',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                registerModel(app, require('./models/storageInfo'))
                cb(null, require('./routes/storageManagement/storageInfo'))
              },
              'storageInfo'
            )
          },
        },
        {
          path: 'webManagement/adManagement',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/adManagement'))
              cb(null, require('./routes/webManagement/adManagement'))
            }, 'adManagement')
          },
        }, {
          path: 'logistics/receiptManagement',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/receiptManagement'))
              cb(null, require('./routes/logistics/receiptManagement'))
            }, 'receiptManagement')
          },
        }, {
          path: 'logistics/loadGoods/carStart',
          getComponent (nextState, cb) {
            require.ensure([], require => {
              registerModel(app, require('./models/carStart'))
              cb(null, require('./routes/logistics/loadGoods/carStart'))
            }, 'carStart')
          },
        }, {
          path: '*',
          getComponent (nextState, cb) {
            require.ensure(
              [],
              require => {
                cb(null, require('./routes/error/'))
              },
              'error'
            )
          },
        },
      ],
    },
  ]

  return <Router history={history} routes={routes} />
}

Routers.propTypes = {
  history: PropTypes.object,
  app: PropTypes.object,
}

export default Routers
