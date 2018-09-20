import React from 'react'
import { Card, Table } from 'antd'
import PropTypes from 'prop-types'


const CarList = ({ dispatch, tableTime, dataSource, selectedRowKeys }) => {
  // 选中的方法
  const onChange = (value, data) => {
    dispatch({ type: 'carStart/updateCarHalfwayUnLoad', payload: { selectedRowKeys: value } })
    dispatch({ type: 'carStart/updateCarHalfwayUnLoad', payload: { alreadyChooseData: data } })
  }
  const columnsData = [
    {
      title: '物流编号',
      dataIndex: 'trackingNumber',
      width: 120,
    },
    {
      title: '运单号',
      dataIndex: 'waybillNumber',
      width: 120,
    },
    {
      title: '收件类型',
      dataIndex: 'goodsType',
      width: 80,
      render: (text, record) => {
        if (record.goodsType === '0') {
          text = '零担'
        } else if (record.goodsType === '1') {
          text = '整车'
        } else if (record.goodsType === '2') {
          text = '快递'
        }
        return (
          <span>{text}</span>
        )
      },
    },
    {
      title: '发站城市',
      dataIndex: 'originArea',
      width: 100,
    },
    {
      title: '到站城市  ',
      dataIndex: 'destArea',
      width: 100,
    },
    {
      title: '货物名称',
      dataIndex: 'commodityName',
      width: 120,
    },
    {
      title: '装车数量',
      dataIndex: 'waybillQuantity',
      width: 120,
    },
    {
      title: '收货单位',
      dataIndex: 'receiverUserCompanyName',
      width: 120,
    },
    {
      title: '收货电话',
      dataIndex: 'receiverUserTelephone',
      width: 120,
    },
    {
      title: '包装',
      dataIndex: 'packageUnitValue',
      width: 120,
    },
    {
      title: '重量KG',
      dataIndex: 'weight',
      width: 120,
      render: (text, record) => {
        if (record.trackingNumber === '合计') {
          record.weight = record.weight
        } else {
          record.weight = record.singleWeight * record.waybillQuantity
        }
        return (
          <span>{record.weight}</span>
        )
      },
    },
    {
      title: '体积m³',
      dataIndex: 'volume',
      width: 120,
      render: (text, record) => {
        if (record.trackingNumber === '合计') {
          record.volume = record.volume
        } else {
          record.volume = record.singleVolume * record.waybillQuantity
        }
        return (
          <span>{record.volume}</span>
        )
      },
    },
    {
      title: '已付',
      dataIndex: 'alreadyPay',
      width: 80,
    },
    {
      title: '提付',
      dataIndex: 'putPay',
      width: 100,
    },
    {
      title: '回付',
      dataIndex: 'backPay',
      width: 80,
    },
    {
      title: '代收货款',
      dataIndex: 'onCollection',
      width: 100,
    },
    {
      title: '垫付费用',
      dataIndex: 'advancePayment',
      width: 120,
    },
    {
      title: '付款方式',
      dataIndex: 'paymentMethodName',
      width: 120,
    },
    {
      title: '备注',
      dataIndex: 'remarks',
      width: 120,
    },
  ]
  let tableProps = {
    columns: columnsData,
    dataSource,
    tableTime,
  }
  let rowSelections = {
    type: 'radio',
    onChange,
    selectedRowKeys,
  }
  return (
    <Card>
      <Table {...tableProps}
        scroll={{ x: 1800 }}
        rowSelection={rowSelections}
      />
    </Card>
  )
}
CarList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  firmModal: PropTypes.obj,
  dataSource: PropTypes.array,
  selectedRowKeys: PropTypes.array,
  trackingNumberAll: PropTypes.array,
}
export default CarList
