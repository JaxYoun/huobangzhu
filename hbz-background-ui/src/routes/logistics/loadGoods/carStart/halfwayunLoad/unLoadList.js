import React from 'react'
import { Card, Table } from 'antd'
import PropTypes from 'prop-types'


const UnLoadList = ({ dispatch, tableTime, dataSource, selectedRowKeys }) => {
  // 选中的方法
  const onChange = (value, data) => {
    dispatch({ type: 'carStart/updateCarHalfway', payload: { selectedRowKeys: value } })
    dispatch({ type: 'carStart/updateCarHalfway', payload: { alreadyChooseData: data } })
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
      title: '货物编号',
      dataIndex: 'commodityNum',
      width: 120,
    },
    {
      title: '卸货数量',
      dataIndex: 'waybillQuantity',
      width: 120,
    },
  ]
  let tableProps = {
    columns: columnsData,
    dataSource,
    tableTime,
  }
  return (
    <Card>
      <Table {...tableProps}
        scroll={{ x: 800 }}
      />
    </Card>
  )
}
UnLoadList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  firmModal: PropTypes.obj,
  dataSource: PropTypes.array,
  selectedRowKeys: PropTypes.array,
  trackingNumberAll: PropTypes.array,
}
export default UnLoadList
