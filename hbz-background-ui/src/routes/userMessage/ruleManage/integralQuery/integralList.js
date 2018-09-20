import React from 'react'
import { Card } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import config from 'config'

const { adjust } = config

const IntegralList = ({
  dispatch,
  fields,
  tableTime,
}) => {
  const columnsData = [
    {
      title: '编号',
      dataIndex: 'recNo',
    },
    {
      title: '分手增减',
      dataIndex: 'delta',
    },
    {
      title: '规则名称',
      dataIndex: 'action',
    },
    {
      title: '适用业务',
      dataIndex: 'adjustType',
    },
    {
      title: '积分说明',
      dataIndex: 'msg',
    },
    {
      title: '积分时间',
      dataIndex: 'time',
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/scoreChange/queryPage',
    fields: fields || {},
    tableTime,
  }
  return (
    <div>
      <Card>
        <Ttable {...tableProps} />
      </Card>
    </div>
  )
}

IntegralList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
}
export default IntegralList
