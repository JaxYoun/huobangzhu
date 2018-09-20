import React from 'react'
import PropTypes from 'prop-types'
import pathToRegexp from 'path-to-regexp'
import { connect } from 'dva'
import { Layout, Loader } from 'components'
import { classnames, config } from 'utils'
import { Helmet } from 'react-helmet'
import NProgress from 'nprogress'
import '../themes/index.less'
import './app.less'
import Error from './error'

const { prefix, openPages, headMenu, COOKIE } = config

const { Header, Footer, Sider, styles, ModalLogin, HeadMenu, EnterprisePage } = Layout
let lastHref


const getPermissionMenu = (pathname, menus) => {
  let permissionMenu = []
  const headerMenuPath = {
    consignorEnterprise: 'EnterpriseConsignor',
    driverEnterprise: 'EnterpriseDriver',
    personalCenter: 'EnterprisePersonalCenter',
    storageCenter: 'EnterpriseStorageCenter',
  }
  const menuList = headerMenuPath[pathname.split('/')[1]]
  if (menuList) {
    permissionMenu = menus[menuList]
  }
  return permissionMenu
}

const App = ({ children, dispatch, app, loading, location }) => {
  const {
    user,
    siderFold,
    darkTheme,
    isNavbar,
    menuPopoverVisible,
    navOpenKeys,
    permissions,
    modalLogin,
    menus,
  } = app

  let permissionMenu = []
  let { pathname } = location
  pathname = pathname.startsWith('/') ? pathname : `/${pathname}`
  const { iconFontJS, iconFontCSS, logo } = config

  // 判断访问权限
  const isOpenPage = openPages && openPages.includes(pathname) // 是否无需登陆
  if (!isOpenPage) {
    permissionMenu = getPermissionMenu(pathname, menus)
  }

  const href = window.location.href
  if (lastHref !== href) {
    NProgress.start()
    if (!loading.global) {
      NProgress.done()
      lastHref = href
    }
  }
  const handleLogin = (params) => {
    dispatch({ type: 'app/handalModalLogin', payload: params })
  }
  const headerProps = {
    user,
    isNavbar,
    menuPopoverVisible,
    navOpenKeys,
    switchMenuPopover () {
      dispatch({ type: 'app/switchMenuPopver' })
    },
    logout () {
      dispatch({ type: 'app/logout' })
    },
    fastLogin () {
      dispatch({ type: 'app/showLogin' })
    },
    switchSider () {
      dispatch({ type: 'app/switchSider' })
    },
    changeOpenKeys (openKeys) {
      dispatch({ type: 'app/handleNavOpenKeys', payload: { navOpenKeys: openKeys } })
    },
  }
  const menusProps = {
    menu: headMenu,
    siderFold,
    darkTheme,
    location,
    navOpenKeys,
    changeOpenKeys (openKeys) {
      localStorage.setItem(`${prefix}navOpenKeys`, JSON.stringify(openKeys))
      dispatch({ type: 'app/handleNavOpenKeys', payload: { navOpenKeys: openKeys } })
    },
  }
  // if (openPages && openPages.includes(pathname) || !user.id) {
  //   return (<div>
  //     <Loader spinning={loading.effects['app/query']} />
  //     {children}
  //   </div>)
  // }
  const loginProps = {
    ...modalLogin,
    handleLogin,
    pathname,
    onCancel: () => {
      dispatch({ type: 'app/handalModalLogin', payload: { visible: false } })
    },
    queryMenus: () => {
      dispatch({ type: 'app/query' })
    },
    queryPersonalCenter: () => {
      dispatch({ type: 'personalCenter/getUserInfo' })
    },
  }

  const enterpriseProps = {
    menus: permissionMenu,
    children,
    loading,
  }
  let content = children
  if (!isOpenPage) {
    content = permissionMenu.length ? <EnterprisePage {...enterpriseProps} /> : <Error />
  }
  return (
    <div>
      {/* <Loader spinning={loading.effects['app/query']} /> */}
      <Helmet>
        <title>TROY</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <link rel="icon" href={logo} type="image/x-icon" />
        {iconFontJS && <script src={iconFontJS} />}
        {iconFontCSS && <link rel="stylesheet" href={iconFontCSS} />}
      </Helmet>
      <div className={classnames(styles.layout, { [styles.fold]: isNavbar ? false : siderFold }, { [styles.withnavbar]: isNavbar })}>
        {/* {!isNavbar ? <aside className={classnames(styles.sider, { [styles.light]: !darkTheme })}>
          <Sider {...siderProps} />
        </aside> : ''} */}
        <div className={styles.main}>
          <Header {...headerProps} />
          <HeadMenu {...menusProps} />
          <div className="container">
            <div className={styles.content}>
              {content}
            </div>
          </div>
          {modalLogin.visible && <ModalLogin {...loginProps} />}
          <Footer />
        </div>
      </div>
    </div>
  )
}

App.propTypes = {
  children: PropTypes.element.isRequired,
  location: PropTypes.isRequired,
  dispatch: PropTypes.isRequired,
  app: PropTypes.isRequired,
  loading: PropTypes.isRequired,
  modalLogin: PropTypes.object,
}

export default connect(({ app, loading }) => ({ app, loading }))(App)
