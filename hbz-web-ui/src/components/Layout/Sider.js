import React from 'react'
import PropTypes from 'prop-types'
import Menus from './Menu'

const Sider = ({ siderFold, darkTheme, location, changeTheme, navOpenKeys, changeOpenKeys, headMenu }) => {
  const menusProps = {
    menu: headMenu,
    siderFold,
    darkTheme,
    location,
    navOpenKeys,
    changeOpenKeys,
  }
  return (
    <div>
      {/* <div className={styles.logo}>
        <img alt={'logo'} src={config.logo} />
        {siderFold ? '' : <span>{config.name}</span>}
      </div> */}
      <Menus {...menusProps} />
      {/* {!siderFold ? <div className={styles.switchtheme}>
        <span><Icon type="bulb" />Switch Theme</span>
        <Switch onChange={changeTheme} defaultChecked={darkTheme} checkedChildren="Dark" unCheckedChildren="Light" />
      </div> : ''} */}
    </div>
  )
}

Sider.propTypes = {
  headMenu: PropTypes.array,
  siderFold: PropTypes.bool,
  darkTheme: PropTypes.bool,
  location: PropTypes.object,
  changeTheme: PropTypes.func,
  navOpenKeys: PropTypes.array,
  changeOpenKeys: PropTypes.func,
}

export default Sider
