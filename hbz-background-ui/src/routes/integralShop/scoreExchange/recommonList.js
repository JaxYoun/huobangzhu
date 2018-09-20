import React from 'react'
import { Card, Button, Switch } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import styles from '../common/index.less'
import RecommonModalUpdateForm from './recommonModalUpdate'

const RecommonList = ({ dispatch, fields, tableTime, recommonModalUpdate, wareTypeData }) => {
  // 打开模态框
  const showModal = (data) => {
    dispatch({ type: 'scoreExchange/updateRecommonModalUpdate', payload: { visible: true, firmData: data } })
  }
  // 启用停用推荐
  const onChange = (data, checked) => {
    if (checked) {
      dispatch({ type: 'scoreExchange/updateRecommons', payload: { id: data.id, state: 1 } })
    } else {
      dispatch({ type: 'scoreExchange/updateRecommons', payload: { id: data.id, state: 2 } })
    }
  }
  const columnsData = [
    {
      title: '上架编号',
      dataIndex: 'product.productNo',
      width: 180,
      render: (text, record) => {
        return (
          <div>
            <a onClick={showModal.bind(this, record)}>{text}</a>
          </div>
        )
      },
    },
    {
      title: '上架名称',
      dataIndex: 'product.productName',
      width: 180,
    },
    {
      title: '分类名称',
      dataIndex: 'product.ware.type.name',
      width: 120,
    },
    {
      title: '商品小图',
      dataIndex: 'product.header',
      width: 180,
      render: (text, record) => {
        return (
          <div className={styles.images}> <img src={record.product.header} /> </div>
        )
      },
    },
    {
      title: '推荐图',
      dataIndex: 'headerBit',
      width: 180,
      render: (text, record) => {
        return (
          <div className={styles.images}> <img src={record.headerBit} /> </div>
        )
      },
    },
    {
      title: '推荐位置',
      dataIndex: 'useType',
      width: 120,
      render: (text, record) => {
        if (record.useType === '1') {
          text = '热门'
        } else {
          text = '横幅'
        }
        return (
          <div>{text}</div>
        )
      },
    },
    {
      title: '推荐排序',
      dataIndex: 'index',
      width: 120,
    },
    {
      title: '推荐状态',
      dataIndex: 'state',
      width: 120,
      render: (text, record) => {
        if (record.state === 1) {
          text = '启用'
        } else {
          text = '停用'
        }
        return (
          <div>{text}</div>
        )
      },
    },
    {
      title: '创建人',
      dataIndex: 'createdBy',
      width: 120,
    },
    {
      title: '创建时间',
      dataIndex: 'createdDate',
      width: 200,
    },
    {
      title: '修改人',
      dataIndex: 'lastUpdatedBy',
      width: 120,
    },
    {
      title: '最后修改时间',
      dataIndex: 'lastUpdatedDate',
      width: 120,
    },
    {
      title: '是否启用',
      render: (text, record) => {
        let checked = true
        if (record.state === 2) {
          checked = false
        } else {
          checked = true
        }
        return (
          <div>
            <Switch checked={checked} checkedChildren="启用" unCheckedChildren="停用" onChange={onChange.bind(this, record)} />
          </div>
        )
      },
      width: 200,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/recommendProduct/queryPage',
    fields: fields || {},
    serialNumber: 'serialNumber',
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps}
        scroll={{ x: 1500 }}
      />
      {recommonModalUpdate.visible && <RecommonModalUpdateForm {...recommonModalUpdate} dispatch={dispatch} wareTypeData={wareTypeData} />}
    </Card>
  )
}
RecommonList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  recommonModalUpdate: PropTypes.obj,
  wareTypeData: PropTypes.array,
}
export default RecommonList
