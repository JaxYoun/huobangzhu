import React from 'react'
import { Modal, Tree } from 'antd'
import PropTypes from 'prop-types'
import { arrayToTree, changeKeyNames } from 'utils'
import styles from './index.less'

const TreeNode = Tree.TreeNode

const MenuModal = ({ dispatch, menuModal, loading }) => {
  const { id, roleName, role, menuList = [], roleMenu = [], checkedMenuKeys } = menuModal
  const treeData = arrayToTree(
    changeKeyNames(menuList, { title: 'name', key: 'id' }),
    'id',
    'parentId'
  )
  const onCancel = () => {
    dispatch({
      type: 'roleManagement/update',
      payload: { menuModal: { visible: false } },
    })
  }
  const onOk = () => {
    dispatch({
      type: 'roleManagement/makeMenus',
      payload: { id, menuIds: checkedMenuKeys },
    })
  }
  const confirmLoading = () => {
    let state = false
    if (menuList.length < 1) {
      state = true
    } else {
      state = loading.global
    }
    return state
  }
  const renderTreeNodes = data => {
    return data.map(item => {
      if (item.children) {
        return (
          <TreeNode title={item.title} key={item.key} dataRef={item}>
            {renderTreeNodes(item.children)}
          </TreeNode>
        )
      }
      return <TreeNode {...item} />
    })
  }
  const onCheck = checkedKeys => {
    dispatch({
      type: 'roleManagement/updateMenuKeys',
      payload: { checkedMenuKeys: checkedKeys },
    })
  }
  console.log(checkedMenuKeys)
  return (
    <Modal
      title="绑定菜单"
      visible
      maskClosable={false}
      onCancel={onCancel}
      onOk={onOk}
      className={styles.detailModal}
      confirmLoading={confirmLoading()}
    >
      <div className={styles.userinfo}>
        <p>角色名称：{roleName}</p>
        <p>角色编码：{role}</p>
        <h2>选择菜单：</h2>
        <Tree checkable onCheck={onCheck} checkedKeys={checkedMenuKeys} defaultCheckedKeys={roleMenu}>
          {renderTreeNodes(treeData)}
        </Tree>
      </div>
    </Modal>
  )
}
MenuModal.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  menuModal: PropTypes.object,
  loading: PropTypes.object,
  checkedMenuKeys: PropTypes.array,
}
export default MenuModal
