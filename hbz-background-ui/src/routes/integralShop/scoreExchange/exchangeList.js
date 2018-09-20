import React from 'react'
import { Card, Button } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import moment from 'moment'
import styles from '../common/index.less'
import ExchangeModalForm from './exchangeModal'
import DeliverModalForm from './deliverModal'

const ExchangeList = ({ dispatch, fields, tableTime, exchangeModal, wareTypeData, deliverModal, transData }) => {
  // 打开模态框
  const showModal = (data) => {
    dispatch({ type: 'scoreExchange/updateExchangeModal', payload: { visible: true, firmData: data } })
    dispatch({ type: 'scoreExchange/getSend', payload: { orderNo: data.orderNo } })
  }
  // 发货
  const showDeliverModal = (data) => {
    dispatch({ type: 'scoreExchange/updateDeliverModal', payload: { visible: true, firmData: data } })
  }
  const columnsData = [
    {
      title: '积分兑换订单编号',
      dataIndex: 'orderNo',
      width: 180,
      render: (text, record) => {
        return (<div>
          <a onClick={showModal.bind(this, record)}>{text}</a>
        </div>)
      },
    },
    {
      title: '兑换积分',
      dataIndex: 'product.score',
      width: 180,
    },
    {
      title: '兑换人',
      dataIndex: 'link',
      width: 180,
    },
    {
      title: '联系电话',
      dataIndex: 'telephone',
      width: 180,
    },
    {
      title: '送货地址',
      dataIndex: 'destAddr',
      width: 180,
    },
    {
      title: '兑换时间',
      dataIndex: 'createdDate',
      width: 150,
      render: (text, record) => {
        text = record.createdDate && moment(record.createdDate).format('YYYY-MM-DD HH:mm:ss')
        return (
          <div>{text}</div>
        )
      },
    },
    {
      title: '是否已发货',
      dataIndex: 'state',
      width: 120,
      render: (text, record) => {
        if (record.state === 1) {
          text = '未发货'
        } else if (record.state === 2) {
          text = '已发货'
        } else {
          text = '收货完'
        }
        return (
          <div>{text}</div>
        )
      },
    },
    {
      title: '发货时间',
      dataIndex: 'sendTime',
      render: (text, record) => {
        text = record.sendTime && moment(record.sendTime).format('YYYY-MM-DD HH:mm:ss')
        return (
          <div>{text}</div>
        )
      },
      width: 150,
    },
    {
      title: '商品名称',
      dataIndex: 'product.ware.name',
      width: 120,
    },
    {
      title: '分类名称',
      dataIndex: 'product.ware.type.name',
      width: 120,
    },
    {
      title: '上架数量',
      dataIndex: 'product.total',
      width: 120,
    },
    {
      title: '品牌',
      dataIndex: 'product.ware.brand.name',
      width: 120,
    },
    {
      title: '规格',
      dataIndex: 'product.ware.specifications',
      width: 120,
    },
    {
      title: '参考售价',
      dataIndex: 'product.ware.marketAmount',
      width: 120,
    },
    {
      title: '操作',
      render: (text, record) => {
        if (record.state === 1) {
          return (
            <div>
              <Button onClick={showDeliverModal.bind(this, record)}>发货</Button>
            </div>
          )
        } else {
          return (
            <div>
              <Button onClick={showDeliverModal.bind(this, record)} disabled>发货</Button>
            </div>
          )
        }
      },
      width: 100,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/scoreOrder/queryPage',
    fields: fields || {},
    serialNumber: 'serialNumber',
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps}
        scroll={{ x: 2000 }}
      />
      {exchangeModal.visible && <ExchangeModalForm {...exchangeModal}
        dispatch={dispatch}
        wareTypeData={wareTypeData}
        transData={transData}
      />}
      {deliverModal.visible && <DeliverModalForm {...deliverModal}
        dispatch={dispatch}
        wareTypeData={wareTypeData}
        transData={transData}
        transData={transData}
      />}
    </Card>
  )
}
ExchangeList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  exchangeModal: PropTypes.obj,
  wareTypeData: PropTypes.array,
  deliverModal: PropTypes.obj,
  transData: PropTypes.obj,
}
export default ExchangeList
