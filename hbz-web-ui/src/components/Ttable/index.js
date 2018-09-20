import React from 'react'
import { Table } from 'antd'
import PropTypes from 'prop-types'
import { request } from 'utils'
const isEmptyObject = (obj) => {
  for (let i in obj)
    return false
  return true
}
class Ttable extends React.Component {

  constructor (props) {
    super(props)
    let pagination = {
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: total => `共 ${total} 条`,
      total: 0,
      defaultCurrent: 1,
      current: 1,
      defaultPageSize: 10,
      pageSize: 10,
      size: '',
      reload: false,
    }
    Object.assign(pagination, props.pagination)
    this.state = {
      dataSource: [],
      pagination,
      loading: false,
      fields: {},
      selectedRowKeys: [],
    }
  }
  getDefaultProps () {
    return {
      disLoad: false,
      tableTime: '',
    }
  }
  componentDidMount () {
    let { disLoad } = this.props
    !disLoad && this.handleRequest() // 如果没有设置disLoad 或 disLoad 为false时 直接获取数据， 若 disLoad 为false 则需手动刷新
  }
  componentDidUpdate (prevProps, prevState) { // fields 发生改变时刷新表格
    let prevfields = prevProps.fields
    let prevPage = prevState.pagination
    let prevtableTime = prevProps.tableTime
    let { fields, tableTime } = this.props
    let { pagination } = this.state
    if (JSON.stringify(prevfields) !== JSON.stringify(fields) || prevPage.pageSize !== pagination.pageSize) {  // 当模态框从隐藏到显示时促发
      this.reField(fields)
    }
    tableTime !== prevtableTime && this.reField(fields)
  }
  onSelectChange = (selectedRowKeys, selectedRows) => {
    const { rowSelection } = this.props
    rowSelection && rowSelection.onChange && rowSelection.onChange(selectedRowKeys, selectedRows)
    this.setState({ selectedRowKeys })
  }
  handleTableChange = (pagination, filters, sorter) => {
    let { fields } = this.state
    this.handleRequest({ ...fields, ...filters }, pagination)
  }
  reField = (params = {}, pager = {}) => {
    let { pagination } = this.state
    pagination.current = 1
    this.handleRequest(params, pager)
  }
  reload = () => {
    let { fields } = this.state
    this.handleRequest(fields)
  }
  handleRequest = (params = {}, pager = {}) => {
    let { sourceUrl, fields, loadedBack, requiredFields } = this.props
    //  判断有没有必选参数， 如果有，但是没有满足必选参数的话就清空数据
    let isFieldsSatisfy = true
    if (requiredFields && requiredFields.length) {
      for (let fieldsName of requiredFields) {
        if (!fields[fieldsName]) {
          isFieldsSatisfy = false
          break
        }
      }
    }
    if (!isFieldsSatisfy) {
      this.clearTableData()
      return
    }
    let { pagination } = this.state
    this.setState({ loading: true })
    if (!isEmptyObject(params)) {
      fields = params
    }
    let pageinfo = {
      page: (pager.current || pagination.current) - 1,
      size: pager.pageSize || pagination.pageSize || 10,
    }
    sourceUrl = `${sourceUrl}?page=${pageinfo.page}&size=${pageinfo.size}`
    request(sourceUrl, {
      body: JSON.stringify({
        ...fields,
        ...pageinfo,
      }),
      showMsg: false,
    }).then((json) => {
      setTimeout(() => {
        loadedBack && loadedBack(json, pageinfo)
        let data = json.data || {}
        Object.assign(pagination, pager)
        pagination.total = data.totalElements
        pagination.pageSize = data.size
        this.setState({
          loading: false,
          reload: false,
          pagination,
          dataSource: data.content,
          fields,
          selectedRowKeys: [],
        })
      }, 100)
    })
  }
  clearTableData = () => {
    const { pagination } = this.state
    pagination.total = 0
    this.setState({ dataSource: [] })
  }
  render () {
    const { pagination, loading, dataSource, selectedRowKeys } = this.state
    let { serialNumber, serialNumberCol, columns, rowSelection } = this.props
    let beforCol = []
    serialNumber && beforCol.push({
      title: '序号',
      dataIndex: 'serialNumber',
      width: '50',
      render: (text, record, index) => {
        let { pagination: pageinfo } = this.state
        return index + 1 + (pageinfo.current - 1) * pageinfo.pageSize
      },
      ...serialNumberCol,
    })

    const tableDefault = {
      rowKey: record => record.registered,
      bordered: true,
      dataSource,
      loading,
      pagination,
      onChange: this.handleTableChange,
    }
    const tableProps = {
      columns: [...beforCol, ...columns],
      rowSelection: rowSelection ? { selectedRowKeys, ...rowSelection, onChange: this.onSelectChange } : false,
    }
    return (<Table {...tableDefault} {...this.props} {...tableProps} />)
  }
}
Ttable.propTypes = {
  sourceUrl: PropTypes.string,
  columns: PropTypes.array,
  fields: PropTypes.object,
  pagination: PropTypes.object,
  getMaster: PropTypes.func,
  disLoad: PropTypes.bool,
  loadedBack: PropTypes.func,
  tableTime: PropTypes.string,
  serialNumber: PropTypes.bool,
  serialNumberCol: PropTypes.object,
  rowSelection: PropTypes.object || PropTypes.bool,
  requiredFields: PropTypes.array,
}

export default Ttable
