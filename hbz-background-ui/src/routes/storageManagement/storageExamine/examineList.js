import React from 'react'
import { Card, Button, Popconfirm } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import moment from 'moment'
import ExamineModal from './examinModal'
import ExamineModalDetailForm from './examinModalDetail'

const ExamineList = ({ dispatch, fields, tableTime, firmModal, detailModal }) => {
  // 打开模态框
  const showModal = (data) => {
    dispatch({ type: 'storageExamine/getdetailData', payload: { id: data.id } })
  }
  // 审核通过
  const passPut = (data) => {
    dispatch({ type: 'storageExamine/updatefirmModal', payload: { visible: true, type: '1', firmData: data } })
  }
  // 审核不通过
  const passPutNo = (data) => {
    dispatch({ type: 'storageExamine/updatefirmModal', payload: { visible: true, type: '2', firmData: data } })
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
      title: '审核状态',
      dataIndex: 'type',
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
      dataIndex: 'recordComment',
      width: 200,
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
    sourceUrl: '/manager/queryWarehouseAudit',
    fields: fields || {},
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps}
        scroll={{ x: 1300 }}
      />
      {firmModal.visible && <ExamineModal {...firmModal}
        dispatch={dispatch}
      />}
      {detailModal.visible && <ExamineModalDetailForm {...detailModal}
        dispatch={dispatch}
      />}
    </Card>
  )
}
ExamineList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  firmModal: PropTypes.obj,
  detailModal: PropTypes.obj,
}
export default ExamineList
