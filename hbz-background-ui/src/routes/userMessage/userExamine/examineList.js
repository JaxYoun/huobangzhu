import React from 'react'
import { Card, Button, Popconfirm } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import ExamineModal from './examineModal'

const ExamineList = ({ dispatch, fields, tableTime, firmModal, registryData,
  registryProgressData, certificateTypeData, transTypeData }) => {
  // 打开模态框
  const showModal = (data) => {
    dispatch({ type: 'userExamine/getData', payload: { id: data.id } })
    dispatch({ type: 'userExamine/updatefirmModal', payload: { visible: true } })
  }
  // 审核通过
  const passPut = (data) => {
    dispatch({ type: 'userExamine/passPut', payload: { id: data.id, registryProgress: 'REGISTRYED' } })
  }
  // 审核不通过
  const passPutNo = (data) => {
    dispatch({ type: 'userExamine/passPut', payload: { id: data.id, registryProgress: 'ERR_REGISTER' } })
  }
  const columnsData = [
    {
      title: '客户id',
      dataIndex: 'id',
      width: 80,
      render: (text, record) => {
        return (
          <a onClick={showModal.bind(this, record)} >{text}</a>
        )
      },
    },
    {
      title: '认证类型',
      dataIndex: 'registryCodeValue',
      width: 80,
    },
    {
      title: '昵称',
      dataIndex: 'user.nickName',
      width: 100,
    },
    {
      title: '帐号',
      dataIndex: 'user.login',
      width: 100,
    },
    {
      title: '代表名称',
      dataIndex: 'owerName',
      width: 80,
    },
    // {
    //   title: '代表证件号',
    //   dataIndex: '',
    //   width: 100,
    // },
    {
      title: '审核状态',
      dataIndex: 'registryProgressVal',
      width: 80,
    },
    {
      title: '联系电话',
      dataIndex: 'user.telephone',
      width: 120,
    },
    {
      title: '创建日期',
      dataIndex: 'createdDate',
      width: 100,
    },
    {
      title: '操作',
      render: (text, record) => {
        return (
          <div>
            <Popconfirm title="确定审核通过吗？" onConfirm={passPut.bind(this, record)}>
              <Button>审核通过</Button>
            </Popconfirm>
            <Popconfirm title="确定审核不通过吗？" onConfirm={passPutNo.bind(this, record)}>
              <Button>审核不通过</Button>
            </Popconfirm>
          </div>
        )
      },
      width: 150,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/user/registry/queryPage',
    fields: fields || {},
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps}
        scroll={{ x: 1300 }}
      />
      {firmModal.visible && <ExamineModal {...firmModal} certificateTypeData={certificateTypeData}
        registryData={registryData} registryProgressData={registryProgressData} dispatch={dispatch}
        transTypeData={transTypeData}
      />}
    </Card>
  )
}
ExamineList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  firmModal: PropTypes.obj,
  registryData: PropTypes.array,
  registryProgressData: PropTypes.array,
  certificateTypeData: PropTypes.array,
  transTypeData: PropTypes.array,
}
export default ExamineList
