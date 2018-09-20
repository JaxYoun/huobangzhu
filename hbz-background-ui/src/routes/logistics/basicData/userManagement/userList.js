import React from 'react'
import { Card, Button, Popconfirm } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import UserModal from './userModal'

const UserList = ({ dispatch, fields, tableTime, firmModal, bankData, userClassification }) => {
  // 打开模态框
  const showModal = (data) => {
    dispatch({ type: 'userManagement/updatefirmModal', payload: { visible: true, firmData: data } })
  }
  // 删除
  const deleteUser = (data) => {
    dispatch({ type: 'userManagement/deleteUsers', payload: { id: data.id } })
  }
  const columnsData = [
    {
      title: '单位名称',
      dataIndex: 'companyName',
      width: 100,
    },
    {
      title: '客户分类',
      dataIndex: 'userClassificationValue',
      width: 80,
    },
    {
      title: '客户名称',
      dataIndex: 'userName',
      width: 100,
    },
    {
      title: '联系电话',
      dataIndex: 'userTelephone',
      width: 120,
    },
    {
      title: '身份证号',
      dataIndex: 'idCard',
      width: 100,
    },
    {
      title: '地址',
      dataIndex: 'userAddress',
      width: 120,
    },
    {
      title: '开户行',
      dataIndex: 'bankValue',
      width: 120,
    },
    {
      title: '银行账号',
      dataIndex: 'bankAccount',
      width: 120,
    },
    {
      title: '简拼',
      dataIndex: 'jianpin',
      width: 120,
    },
    {
      title: '备注',
      dataIndex: 'remarks',
      width: 200,
    },
    {
      title: '操作',
      render: (text, record) => {
        return (
          <div>
            <Button onClick={showModal.bind(this, record)}>修改</Button>
            <Popconfirm title="确定要删除吗？" onConfirm={deleteUser.bind(this, record)}>
              <Button>删除</Button>
            </Popconfirm>
          </div>
        )
      },
      width: 200,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/manager/userInformationTable',
    fields: fields || {},
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps}
        scroll={{ x: 1800 }}
      />
      {firmModal.visible && <UserModal {...firmModal} bankData={bankData} dispatch={dispatch} userClassification={userClassification} />}
    </Card>
  )
}
UserList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  firmModal: PropTypes.obj,
  bankData: PropTypes.array,
  userClassification: PropTypes.array,
}
export default UserList
