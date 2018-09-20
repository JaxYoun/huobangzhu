import React from 'react'
import PropTypes from 'prop-types'
import { Form, Row, Col, TreeSelect, Select } from 'antd'
import { address01, changeKeyNames } from 'utils'
const FormItem = Form.Item

class SelectArea extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      provinceData: [],
      cityData: [],
      areaData: [],
    }
  }

  componentDidMount () {
    this.getProvinceData()
  }
  getProvinceData () {
    const treeData = []
    for (let item of address01) {
      if (item.id === 0 || item.parentId === 0) {
        if (item.id === 0 && item.parentId === null) {
          item.selectable = false
        }
        treeData.push(item)
      }
    }
    const Data = changeKeyNames(treeData, { value: 'id', key: 'id', pId: 'parentId', label: 'areaName' })
    this.setState({
      provinceData: Data,
    })
  }
  provinceChange (value) {
    let Data = []
    for (let item of address01) {
      if (item.parentId === value) {
        Data.push(item)
      }
    }
    const city = changeKeyNames(Data, { value: 'id', key: 'id', pId: 'parentId', label: 'areaName' })
    for (let items of city) {
      items.pId = null
    }
    console.log(value)
    if (value === 1 || value === 18 || value === 2234 || value === 790) {
      this.setState({
        areaData: city,
        cityData: [],
      })
    } else {
      this.setState({
        cityData: city,
      })
    }
    this.props.form.resetFields([this.props.city.key, this.props.area.key])
  }
  cityChange (value) {
    let Data = []
    for (let item of address01) {
      if (item.parentId === value) {
        Data.push(item)
      }
    }
    this.setState({
      areaData: Data,
    })
    this.props.form.resetFields([this.props.area.key])
  }
  render () {
    const { province, city, area, form: { getFieldDecorator } } = this.props
    return (
      <div>
        <Row>
          <Col span={7}>
            <FormItem label={province.label}>
              {getFieldDecorator(province.key, {
                rules: [
                  {
                    required: !province.notRequired,
                    message: '请选择省份',
                  },
                ],
              })(
                <TreeSelect
                  showSearch
                  treeDataSimpleMode
                  treeNodeFilterProp="areaName"
                  treeData={this.state.provinceData}
                  onChange={this.provinceChange.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col span={7} offset={1}>
            <FormItem label={city.label}>
              {getFieldDecorator(city.key, {
                rules: [
                  {
                    required: this.state.cityData.length !== 0,
                    message: '请选择城市',
                  },
                ],
              })(
                <TreeSelect
                  allowClear
                  showSearch
                  treeDataSimpleMode
                  treeNodeFilterProp="areaName"
                  treeData={this.state.cityData}
                  onChange={this.cityChange.bind(this)}
                  disabled={this.state.cityData.length === 0}
                />
                // <Select onChange={this.cityChange.bind(this)} disabled={this.state.cityData.length === 0}>
                //   {
                //     this.state.cityData.map(item => (
                //       <Option value={item.id}>{item.areaName}</Option>
                //     ))
                //   }
                // </Select>
              )}
            </FormItem>
          </Col>
          <Col span={7} offset={1}>
            <FormItem label={area.label}>
              {getFieldDecorator(area.key, {
                rules: [
                  {
                    required: !area.notRequired,
                    message: '请选择区域',
                  },
                ],
              })(
                <Select onBlur={this.props.TypeChange}>
                  {
                    this.state.areaData.map(item => (
                      <Option value={item.outCode}>{item.areaName}</Option>
                    ))
                  }
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
      </div>
    )
  }
}
SelectArea.propTypes = {
  province: PropTypes.object,
  form: PropTypes.object,
  city: PropTypes.object,
  area: PropTypes.object,
  TypeChange: PropTypes.func,
}

export default SelectArea
