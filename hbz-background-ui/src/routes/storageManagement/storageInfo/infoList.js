import React from 'react'
import { Card, Button, Popconfirm } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import moment from 'moment'
import ModalInfoForm from './infoModal'

const InfoList = ({ dispatch, fields, tableTime, firmModal }) => {
  // 打开模态框
  const showModal = (data) => {
    dispatch({ type: 'storageInfo/getfirmData', payload: { id: data.id } })
  }
  // 过期
  const outDate = (data) => {
    dispatch({ type: 'storageInfo/outDate', payload: { id: data.id } })
  }
  const columnsData = [
    {
      title: '仓库名称',
      dataIndex: 'name',
      width: 120,
      render: (text, record) => {
        return (
          <a onClick={showModal.bind(this, record)} >{text}</a>
        )
      },
    },
    {
      title: '发布时间',
      dataIndex: 'publishDate',
      width: 120,
      render: (text, record) => {
        text = record.publishDate && moment(Number(record.publishDate)).format('YYYY-MM-DD HH:mm:ss')
        return (
          <span>{text}</span>
        )
      },
    },
    {
      title: '状态',
      dataIndex: 'lifecycleValue',
      width: 100,
    },
    {
      title: '审核人',
      dataIndex: 'createUser',
      width: 100,
    },
    {
      title: '审核时间',
      dataIndex: 'checkedDate',
      width: 120,
      render: (text, record) => {
        const checkedDates = moment(Number(record.checkedDate)).format('YYYY-MM-DD HH:mm:ss')
        return (
          <span>{checkedDates}</span>
        )
      },
    },
    {
      title: '备注',
      dataIndex: 'warehouseDescribe',
      width: 200,
    },
    {
      title: '操作',
      render: (text, record) => {
        return (
          <div>
            <Popconfirm title="确定过期吗？" onConfirm={outDate.bind(this, record)}>
              <Button>过期</Button>
            </Popconfirm>
          </div>
        )
      },
      width: 100,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/manager/queryWarehouseManage',
    fields: fields || {},
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps}
        scroll={{ x: 1300 }}
      />
      {firmModal.visible && <ModalInfoForm {...firmModal}
        dispatch={dispatch}
      />}
    </Card>
  )
}
InfoList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  firmModal: PropTypes.obj,
  detailModal: PropTypes.obj,
}
export default InfoList
