import React from 'react'
import PropTypes from 'prop-types'
import { Card, Button, Row } from 'antd'
import { Ttable } from 'components'
import ShowExpress from './showExpress'
import AddExpressForEach from './addExpressForEach'
const HistoryList = ({
  fieldsHistory,
  tableTime,
  dispatch,
  expressShowModal,
  addExpressForEachModal,
}) => {
  // 打开模态框
  const showModal = (data) => {
    dispatch({ type: 'expressOrder/showDataGet', payload: { id: data.exId } })
    dispatch({ type: 'expressOrder/updateExpressShowModal', payload: { visible: true } })
  }
  // 打开新增模态框
  const showModals = (data) => {
    dispatch({ type: 'expressOrder/updateAddExpressForEachModal', payload: { visible: true, firmData: data } })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'expressOrder/updateExpressShowModal', payload: { visible: false, expressShowData: [] } })
    dispatch({ type: 'expressOrder/clean' })
  }

    // 编辑订单列表
  const columns = [
    {
      title: '订单编号',
      dataIndex: 'orderNo',
      width: 200,
    },
    {
      title: '快递承运公司',
      dataIndex: 'expressCompanyTypeValue',
      width: 120,
    },
    {
      title: '承运公司快递单号',
      dataIndex: 'trackingNumber',
      width: 120,
    },
    {
      title: '快递派送时间',
      dataIndex: 'sendTime',
      width: 120,
    },
    {
      title: '取件城市',
      dataIndex: 'originArea',
      width: 120,
    },
    {
      title: '收件城市',
      dataIndex: 'destArea',
      width: 200,
    },
    {
      title: '收件联系人',
      dataIndex: 'linker',
      width: 80,
    },
    {
      title: '收件联系电话',
      dataIndex: 'telephone',
      width: 80,
    },
    {
      title: '重量(kg)',
      dataIndex: 'commodityWeight',
      width: 80,
    },
    {
      title: '体积(m³)',
      dataIndex: 'commodityVolume',
      width: 80,
    },
    {
      title: '金额(元)',
      dataIndex: 'amount',
      width: 100,
    },
    {
      title: '操作',
      dataIndex: 'expressCompanyTypeValue ',
      width: 220,
      render: (text, record) => {
        return (
          <div>
            <Button type="success" onClick={showModal.bind(this, record)} >查看物流详情</Button>
            <Button type="primary" style={{ background: '#FF9900', borderColor: '#FF9900' }} onClick={showModals.bind(this, record)} >新增物流详情</Button>
          </div>
        )
      },
    },
  ]

  return (
    <Card>
      <Ttable
        columns={columns}
        fields={fieldsHistory}
        sourceUrl="/manager/logisticsDetailsPage"
        tableTime={tableTime}
        scroll={{ x: 2200 }}
      />
      <ShowExpress {...expressShowModal} hideModal={hideModal} dispatch={dispatch} />
      <AddExpressForEach {...addExpressForEachModal} dispatch={dispatch} />
    </Card>
  )
}

HistoryList.propTypes = {
  tableTime: PropTypes.string,
  fieldsHistory: PropTypes.array,
  dispatch: PropTypes.func,
  expressShowModal: PropTypes.obj,
  addExpressForEachModal: PropTypes.obj,
}
export default HistoryList
