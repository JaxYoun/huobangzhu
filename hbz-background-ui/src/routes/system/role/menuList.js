import React from 'react'
import PropTypes from 'prop-types'
import { Table, Card, Button, Icon } from 'antd'
import { arrayToTree, isLimited } from 'utils'
import LimitMenu from './limitMenu'

const menuList = ({
  onChangeMenu,
  menuData,
  roleMenu,
  saveMenu,
  loading,
  currentLevel,
}) => {
  const columns = [{
    title: '菜单名称',
    dataIndex: 'menuName',
  }, {
    title: '菜单状态',
    dataIndex: 'status',
    render: (text, record) => {
      let cls = 'close-circle-o'
      if (record.status) {
        cls = 'check-circle-o'
      }
      return <Icon type={cls} />
    },
  }, {
    title: '权限等级',
    dataIndex: 'level',
    render: (text, record) => {
      return (
        <LimitMenu record={record} onChange={onChangeMenu} />
      )
    },
  }]
  let keys = [] // 当前选择角色菜单集合
  for (let [index, item] of menuData.entries()) {
    console.log(item,index)
    const key = roleMenu.findIndex((n) => item.id === n.menuId)
    if (key > -1) {
      menuData[index].level = roleMenu[key].level
      keys.push(item.id)
    } else {
      menuData[index].level = 99
    }
  }
  menuData = arrayToTree(menuData, 'id', 'pId')
  const rowSelection = {
    selectedRowKeys: keys,
    onChange: (selectedRowKeys, selectedRows) => {
      onChangeMenu(selectedRows)
    },
  }
  return (
    <Card title={<span>菜单列表</span>} extra={
      isLimited(currentLevel, 'update')
      && <div>
        <Button loading={loading} type="primary" onClick={saveMenu}>保存角色菜单</Button>
      </div>
      }
    >
      <Table
        rowKey="id"
        className="table-tree"
        columns={columns}
        dataSource={menuData}
        pagination={false}
        rowSelection={rowSelection}
      />
    </Card>
  )
}

menuList.propTypes = {
  onChangeMenu: PropTypes.func,
  menuData: PropTypes.array,
  addMenu: PropTypes.func,
  roleMenu: PropTypes.array,
  saveMenu: PropTypes.func,
  loading: PropTypes.bool,
  onChangeLimit: PropTypes.func,
  dispatch: PropTypes.func,
  currentLevel: PropTypes.object,
}
export default menuList
