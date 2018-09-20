import React from 'react'
import { Card } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'

const ReputationList = ({
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
    sourceUrl: '/credit/queryPage',
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

ReputationList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
}
export default ReputationList
