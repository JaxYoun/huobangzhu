import React from 'react'
import { Card, Button, Popconfirm } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'

const TransportOrderList = ({ dispatch, fields, tableTime }) => {
  // 选中的方法
  const onChange = (value, data) => {
    dispatch({ type: 'carStart/updateTransportOrder', payload: { chooseData: data } })
  }
  const columnsData = [
    {
      title: '物流编号',
      dataIndex: 'trackingNumber',
      width: 100,
    },
    {
      title: '运单号',
      dataIndex: 'waybillNumber',
      width: 100,
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
        } else {
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
      title: '到站城市',
      dataIndex: 'destArea',
      width: 100,
    },
    {
      title: '货物编号',
      dataIndex: 'commodityNumber',
      width: 100,
    },
    {
      title: '货物名称',
      dataIndex: 'commodityName',
      width: 80,
    },
    {
      title: '库存数量',
      dataIndex: 'inventoryQuantity',
      width: 60,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/manager/CargoInformationPage',
    fields: fields || {},
    tableTime,
  }
  let rowSelections = {
    type: 'radio',
    onChange,
  }
  return (
    <Card>
      <Ttable {...tableProps}
        scroll={{ x: 1200, y: 600 }}
        rowSelection={rowSelections}
      />
    </Card>
  )
}
TransportOrderList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  loadGoods: PropTypes.func,
}
export default TransportOrderList
