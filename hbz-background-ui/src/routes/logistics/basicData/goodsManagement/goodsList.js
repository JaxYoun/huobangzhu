import React from 'react'
import { Card, Button, Popconfirm } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import GoodsModal from './goodsModal'

const GoodsList = ({ dispatch, fields, tableTime, firmModal, getpackageData }) => {
  // 打开模态框
  const showModal = (data) => {
    dispatch({ type: 'goodsManagement/updatefirmModal', payload: { visible: true, firmData: data } })
  }
  // 删除
  const deleteUser = (data) => {
    dispatch({ type: 'goodsManagement/deleteGoods', payload: { id: data.id } })
  }
  const columnsData = [
    {
      title: '货物编号',
      dataIndex: 'commodityNumber',
      width: 80,
    },
    {
      title: '货物名称',
      dataIndex: 'commodityName',
      width: 100,
    },
    {
      title: '规格',
      dataIndex: 'specification',
      width: 80,
    },
    {
      title: '包装单位',
      dataIndex: 'packageUnitValue',
      width: 100,
    },
    {
      title: '体积（立方米）',
      dataIndex: 'volume',
      width: 120,
    },
    {
      title: '重量（kg）',
      dataIndex: 'weight',
      width: 100,
    },
    {
      title: '实际重量（kg）',
      dataIndex: 'billingWeight',
      width: 100,
    },
    {
      title: '价格（元）',
      dataIndex: 'price',
      width: 120,
    },
    {
      title: '条码',
      dataIndex: 'barcode',
      width: 120,
    },
    {
      title: '货物简拼',
      dataIndex: 'jianpin',
      width: 120,
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
    sourceUrl: '/manager/commodityInformationTable',
    fields: fields || {},
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps}
        scroll={{ x: 1800 }}
      />
      {firmModal.visible && <GoodsModal {...firmModal} dispatch={dispatch} getpackageData={getpackageData} />}
    </Card>
  )
}
GoodsList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  firmModal: PropTypes.obj,
  getpackageData: PropTypes.array,
}
export default GoodsList
