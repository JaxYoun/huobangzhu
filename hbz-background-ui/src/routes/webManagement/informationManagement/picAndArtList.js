import React from 'react'
import { Card, Switch } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import moment from 'moment'
import styles from '../common/index.less'
import PicAndArtModalForm from './picAndArtModal'

const PicAndArtList = ({ dispatch, fields, tableTime, picAndArtModal, infoData }) => {
  // 打开模态框
  const showModal = (data) => {
    dispatch({ type: 'informationManagement/getPicAndArtData', payload: { id: data.id } })
  }
  // 启用停用推荐
  const onChange = (data, checked) => {
    if (checked) {
      dispatch({ type: 'informationManagement/updateNewsStatus', payload: { id: data.id, status: 1 } })
    } else {
      dispatch({ type: 'informationManagement/updateNewsStatus', payload: { id: data.id, status: 0 } })
    }
  }
  const columnsData = [
    {
      title: '信息编号',
      dataIndex: 'newsNo',
      width: 80,
      render: (text, record) => {
        return (
          <a onClick={showModal.bind(this, record)} >{text}</a>
        )
      },
    },
    {
      title: '信息标题',
      dataIndex: 'title',
      width: 120,
    },
    {
      title: '信息小图',
      dataIndex: 'titleImageList',
      width: 100,
      render: (text, record) => {
        return (
          <div className={styles.images}> <img src={record.titleImageList} /> </div>
        )
      },
    },
    {
      title: '信息类型',
      dataIndex: 'newsTypeValue',
      width: 80,
    },
    {
      title: '信息状态',
      dataIndex: 'status',
      width: 80,
      render: (text, record) => {
        if (record.status === '0') {
          text = '停用'
        } else {
          text = '可用'
        }
        return (
          <span>{text}</span>
        )
      },
    },
    {
      title: '信息简述',
      dataIndex: 'summary',
      width: 300,
    },
    {
      title: '发布时间',
      dataIndex: 'publishDate',
      width: 120,
      render: (text, record) => {
        const publishDates = record.publishDate && moment(Number(record.publishDate)).format('YYYY-MM-DD HH:mm:ss')
        return (
          <span>{publishDates}</span>
        )
      },
    },
    {
      title: '是否显示发布时间',
      dataIndex: 'displayPublishDate',
      width: 80,
      render: (text, record) => {
        if (record.displayPublishDate === '0') {
          text = '否'
        } else {
          text = '是'
        }
        return (
          <span>{text}</span>
        )
      },
    },
    {
      title: '是否启用',
      render: (text, record) => {
        let checked = true
        if (record.status === '0') {
          checked = false
        } else {
          checked = true
        }
        return (
          <div>
            <Switch checked={checked} checkedChildren="可用" unCheckedChildren="停用" onChange={onChange.bind(this, record)} />
          </div>
        )
      },
      width: 200,
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/manager/getNewsListByPage',
    fields: fields || {},
    tableTime,
  }

  return (
    <Card>
      <Ttable {...tableProps}
        scroll={{ x: 1300 }}
      />
      {picAndArtModal.visible && <PicAndArtModalForm {...picAndArtModal} infoData={infoData} dispatch={dispatch} />}
    </Card>
  )
}
PicAndArtList.propTypes = {
  dispatch: PropTypes.func,
  tableTime: PropTypes.string,
  fields: PropTypes.object,
  picAndArtModal: PropTypes.obj,
  infoData: PropTypes.array,
}
export default PicAndArtList
