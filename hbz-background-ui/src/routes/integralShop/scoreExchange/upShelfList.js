import React from 'react'
import { Card, Button, Popconfirm } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import styles from '../common/index.less'
import UpShelfModalForm from './upShelfUpdate'
import RecommonModalForm from './recommonModal'

const UpShelfList = ({ dispatch, fields, tableTime, upShelfModal, wareTypeData, recommonModal }) => {
  // 打开模态框
  const showModal = (data) => {
    dispatch({ type: 'scoreExchange/updateupShelfModal', payload: { visible: true, firmData: data } })
  }
  // 下架
  const goodsShelfOff = (data) => {
    dispatch({ type: 'scoreExchange/shelfOffGoods', payload: { id: data.id } })
  }
  // 推荐
  const recommonModalShow = (data) => {
    dispatch({ type: 'scoreExchange/updateRecommonModal', payload: { firmData: data, visible: true } })
  }
  const columnsData = [
    {
      title: '商品上架编号',
      dataIndex: 'productNo',
      width: 180,
      render: (text, record) => {
        if (record.productStatus === 1) {
          return (<div>
            <a onClick={showModal.bind(this, record)}>{text}</a>
          </div>)
        } else {
          return (<div>{text}</div>)
        }
      },
    },
    {
      title: '商品上架名称',
      dataIndex: 'productName',
      width: 180,
    },
    {
      title: '分类名称',
      dataIndex: 'ware.type.name',
      width: 180,
    },
    {
      title: '商品小图',
      dataIndex: 'header',
      width: 180,
      render: (text, record) => {
        return (
          <div className={styles.images}> <img src={record.header} /> </div>
        )
      },
    },
    {
      title: '品牌',
      dataIndex: 'ware.brand.name',
      width: 180,
    },
    {
      title: '规格',
      dataIndex: 'ware.specifications',
      width: 100,
    },
    {
      title: '参考售价',
      dataIndex: 'ware.marketAmount',
      width: 120,
    },
    {
      title: '产地',
      dataIndex: 'ware.productionAddress',
      width: 130,
    },
    {
      title: '商品状态',
      dataIndex: 'state',
      width: 120,
      render: (text, record) => {
        if (record.state === 0) {
          text = '停用'
        } else {
          text = '可用'
        }
        return (
          <div>{text}</div>
        )
      },
    },
    {
      title: '商品上架状态',
      dataIndex: 'productStatus',
      width: 120,
      render: (text, record) => {
        if (record.productStatus === 0) {
          text = '下架'
        } else {
          text = '上架'
        }
        return (
          <div>{text}</div>
        )
      },
    },
    {
      title: '上架数量',
      dataIndex: 'total',
      width: 120,
    },
    {
      title: '剩余数量',
      dataIndex: 'leave',
      width: 120,
    },
    {
      title: '兑换积分',
      dataIndex: 'score',
      width: 120,
    },
    {
      title: '被推荐次数',
      dataIndex: 'recount',
      width: 120,
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
      title: '操作',
      render: (text, record) => {
        if (record.productStatus === 1) {
          return (
            <div>
              <Popconfirm title="确定要下架吗？" onConfirm={goodsShelfOff.bind(this, record)}>
                <Button>下架</Button>
              </Popconfirm>
              <Button onClick={recommonModalShow.bind(this, record)}>商品推荐</Button>
            </div>
          )
        } else {
          return (
            <div>
              <Button onClick={goodsShelfOff.bind(this, record)} disabled>下架</Button>
            </div>
          )
        }
      },
      width: 400,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/product/queryPage',
    fields: fields || {},
    serialNumber: 'serialNumber',
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps}
        scroll={{ x: 2500 }}
      />
      {upShelfModal.visible && <UpShelfModalForm {...upShelfModal} dispatch={dispatch} wareTypeData={wareTypeData} />}
      {recommonModal.visible && <RecommonModalForm {...recommonModal} dispatch={dispatch} wareTypeData={wareTypeData} />}
    </Card>
  )
}
UpShelfList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  upShelfModal: PropTypes.obj,
  wareTypeData: PropTypes.array,
}
export default UpShelfList
