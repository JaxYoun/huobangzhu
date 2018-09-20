import React from 'react'
import { Card, Button, Popconfirm } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import styles from '../common/index.less'
import GoodsAddModalForm from './goodsAdd'
import GoodsShelvesModalForm from './goodsShelves'

const GoodsList = ({ dispatch, fields, tableTime, goodsModal, wareTypeData, goodsShelvesModal }) => {
  // 打开模态框
  const showModal = (data) => {
    dispatch({ type: 'basicData/updateGoodsModal', payload: { visible: true, firmData: data } })
  }
  // 删除
  const deleteGoods = (data) => {
    dispatch({ type: 'basicData/deleteGoods', payload: { id: data.id } })
  }
  // 上架
  const showModalShelf = (data) => {
    dispatch({ type: 'basicData/updateGoodsShelvesModal', payload: { visible: true, firmData: data } })
  }
  const columnsData = [
    {
      title: '商品编号',
      dataIndex: 'wareNo',
      width: 180,
    },
    {
      title: '商品名称',
      dataIndex: 'name',
      width: 180,
    },
    {
      title: '分类名称',
      dataIndex: 'type.name',
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
      dataIndex: 'brand.name',
      width: 180,
    },
    {
      title: '规格',
      dataIndex: 'specifications',
      width: 100,
    },
    {
      title: '参考售价',
      dataIndex: 'marketAmount',
      width: 120,
    },
    {
      title: '产地',
      dataIndex: 'productionAddress',
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
      width: 80,
    },
    {
      title: '最后修改时间',
      dataIndex: 'lastUpdatedDate',
      width: 120,
    },
    {
      title: '操作',
      render: (text, record) => {
        return (
          <div>
            <Button onClick={showModal.bind(this, record)}>修改</Button>
            <Popconfirm title="确定要删除吗？" onConfirm={deleteGoods.bind(this, record)}>
              <Button>删除</Button>
            </Popconfirm>
            <Button onClick={showModalShelf.bind(this, record)}>上架</Button>
          </div>
        )
      },
      width: 400,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/wareInfo/queryPage',
    fields: fields || {},
    serialNumber: 'serialNumber',
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps}
        scroll={{ x: 2000 }}
      />
      {goodsModal.visible && <GoodsAddModalForm {...goodsModal} dispatch={dispatch} wareTypeData={wareTypeData} />}
      {goodsShelvesModal.visible && <GoodsShelvesModalForm {...goodsShelvesModal} dispatch={dispatch} wareTypeData={wareTypeData} />}
    </Card>
  )
}
GoodsList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  modalState: PropTypes.object,
  goodsModal: PropTypes.obj,
  wareTypeData: PropTypes.array,
  goodsShelvesModal: PropTypes.obj,
}
export default GoodsList
