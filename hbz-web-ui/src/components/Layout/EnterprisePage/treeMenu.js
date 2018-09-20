import React from 'react'
import PropTypes from 'prop-types'
import { Menu, Icon } from 'antd'
import { Iconfont } from '../index'
import { Link } from 'dva/router'
import { arrayToTree, queryArray } from 'utils'
import pathToRegexp from 'path-to-regexp'
const fontSize = { 'font-size': '14px' }

const Menus = ({ siderFold, menu = [] }) => {
  // 生成树状
  const menuTree = arrayToTree(menu, 'id', 'parentId')
  // const menuTree = arrayToTree(menu.filter(_ => _.mpid !== '-1'), 'id', 'mpid')
  const levelMap = {}

  // 递归生成菜单
  const getMenus = (menuTreeN, siderFoldN) => {
    return menuTreeN.map(item => {
      if (item.children) {
        if (item.mpid) {
          levelMap[item.id] = item.mpid
        }
        return (
          <Menu.SubMenu
            key={item.id}
            title={<span style={fontSize}>
              {item.icon && <Iconfont type={item.icon} />}&nbsp;
              {(!siderFoldN || !menuTree.includes(item)) && item.name}
            </span>}
          >
            {getMenus(item.children, siderFoldN)}
          </Menu.SubMenu>
        )
      }
      return (
        <Menu.Item key={item.id} style={fontSize} >
          <Link to={item.route}>
            {item.icon && <Iconfont type={item.icon} />}&nbsp;
            {(!siderFoldN || !menuTree.includes(item)) && item.name}
          </Link>
        </Menu.Item>
      )
    })
  }
  const menuItems = getMenus(menuTree, siderFold)

  // 寻找选中路由
  let currentMenu
  let defaultSelectedKeys
  for (let item of menu) {
    if (item.route && pathToRegexp(item.route).exec(location.pathname)) {
      currentMenu = item
      break
    }
  }

  const getPathArray = (array, current, pid, id) => {
    let result = [String(current[id])]
    const getPath = (item) => {
      if (item && item[pid]) {
        result.unshift(String(item[pid]))
        getPath(queryArray(array, item[pid], id))
      }
    }
    getPath(current)
    return result
  }
  if (currentMenu) {
    defaultSelectedKeys = getPathArray(menu, currentMenu, 'mpid', 'id')
  }
  return (
    <Menu
      mode="inline"
      defaultOpenKeys={['14', '36', '18', '25', '46']}
      theme={'light'}
      defaultSelectedKeys={defaultSelectedKeys}
      key={defaultSelectedKeys}
    >
      {menuItems}
    </Menu>
  )
}

Menus.propTypes = {
  menu: PropTypes.array,
  siderFold: PropTypes.bool,
  darkTheme: PropTypes.bool,
  isNavbar: PropTypes.bool,
  handleClickNavMenu: PropTypes.func,
  navOpenKeys: PropTypes.array,
  changeOpenKeys: PropTypes.func,
}

export default Menus
