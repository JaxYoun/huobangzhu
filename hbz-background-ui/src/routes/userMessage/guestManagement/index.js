import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Button, Switch } from 'antd'
import { Ttable } from 'components'
import Filter from './filter'
import GuestModal from './guestModal'
import RoleModal from './roleModal'
import ExamineModal from './examineModal'

const GuestManagement = ({ loading, dispatch, guestManagement }) => {
  const {
    fields,
    tableTime,
    guestModal,
    roleModal,
    firmModal,
    certificateTypeData,
    registryData,
    registryProgressData,
    transTypeData,
  } = guestManagement
  const getRegisterInfo = (data, type) => {
    console.log(type)
    const record = data.registries.find(item => item.registryCode === type)
    dispatch({ type: 'guestManagement/getData', payload: { id: record.id } })
    dispatch({
      type: 'guestManagement/updatefirmModal',
      payload: { visible: true },
    })
  }
  const renderRegister = (type, record) => {
    const unRegisterText = '未认证'
    let t = {}
    let renderDOM
    if (record.registries.length > 0) {
      t = record.registries.find(n => n.registryCode === type)
    }
    if (!t || !t.registryProgressVal) {
      renderDOM = <span>{unRegisterText}</span>
    } else {
      renderDOM = (
        <Button onClick={getRegisterInfo.bind(this, record, type)}>
          {t.registryProgressVal}
        </Button>
      )
    }
    return renderDOM
  }
  const makeRolesGuest = record => {
    dispatch({
      type: 'guestManagement/update',
      payload: {
        roleModal: {
          visible: true,
          ...record,
        },
      },
    })
    dispatch({
      type: 'guestManagement/queryRole',
      payload: { userId: record.id },
    })
  }
  const switchActive = (id, checked) => {
    let url = 'enableGuest'
    if (!checked) {
      url = 'disableGuest'
    }
    dispatch({
      type: `guestManagement/${url}`,
      payload: { id },
    })
  }
  const editGuest = record => {
    dispatch({
      type: 'guestManagement/update',
      payload: { guestModal: { visible: true, ...record } },
    })
  }
  let tableProps = {
    columns: [
      {
        title: '编号',
        dataIndex: 'id',
      },
      {
        title: '客户昵称',
        dataIndex: 'nickName',
      },
      {
        title: '客户头像',
        dataIndex: '',
      },
      {
        title: '电话号码',
        dataIndex: 'telephone',
      },
      {
        title: '客户状态',
        dataIndex: 'activated',
        render: (text, record) => {
          return (
            <Switch
              checkedChildren="启用"
              unCheckedChildren="停用"
              checked={text}
              onChange={switchActive.bind(this, record.id)}
            />
          )
        },
      },
      {
        title: '配送员认证',
        dataIndex: 'DeliveryBoy',
        render: (text, record) => {
          return renderRegister('DeliveryBoy', record)
        },
      },
      {
        title: '个人货主认证',
        dataIndex: 'PersonConsignor',
        render: (text, record) => {
          return renderRegister('PersonConsignor', record)
        },
      },
      {
        title: '个人司机认证',
        dataIndex: 'PersonDriver',
        render: (text, record) => {
          return renderRegister('PersonDriver', record)
        },
      },
      {
        title: '企业货主认证',
        dataIndex: 'EnterpriseConsignor',
        render: (text, record) => {
          return renderRegister('EnterpriseConsignor', record)
        },
      },
      {
        title: '运输企业认证',
        dataIndex: 'TransEnterprise',
        render: (text, record) => {
          return renderRegister('TransEnterprise', record)
        },
      },
      {
        title: '操作',
        dataIndex: '',
        render: (text, record) => {
          return (
            <div>
              <Button
                type="primary"
                onClick={makeRolesGuest.bind(this, record)}
              >
                分配角色
              </Button>
              <Button onClick={editGuest.bind(this, record)}>编辑</Button>
            </div>
          )
        },
      },
    ],
    sourceUrl: '/user/queryPage',
    fields: fields || {},
    tableTime,
  }
  const guestModalProps = {
    guestModal,
    dispatch,
    loading,
  }
  const examineModalProps = {
    ...firmModal,
    registryData,
    certificateTypeData,
    registryProgressData,
    transTypeData,
    dispatch,
  }
  const roleModalProps = {
    roleModal,
    dispatch,
    loading,
  }
  return (
    <div>
      <Filter dispatch={dispatch} />
      <Card>
        <Ttable {...tableProps} />
      </Card>
      {guestModal.visible && <GuestModal {...guestModalProps} />}
      {roleModal.visible && <RoleModal {...roleModalProps} />}
      {firmModal.visible && <ExamineModal {...examineModalProps} />}
    </div>
  )
}

GuestManagement.propTypes = {
  dispatch: PropTypes.func.isRequired,
  guestManagement: PropTypes.obj,
  loading: PropTypes.bool,
  userExamine: PropTypes.object,
}
export default connect(({ guestManagement, loading }) => ({
  guestManagement,
  loading,
}))(GuestManagement)
