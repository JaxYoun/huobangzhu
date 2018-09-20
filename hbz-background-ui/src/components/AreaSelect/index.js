import React from 'react'
import PropTypes from 'prop-types'
import { Form, Row, Col, Cascader, Input } from 'antd'
import { address01, arrayToTree } from 'utils'

const FormItem = Form.Item

class AreaSelect extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      options: [],
      outCode: props.code ? props.code : '',
      num: 0,
      updateData: props.updateData,
    }
  }
  componentDidMount () {
    this.init()
  }

  componentWillReceiveProps () {
    this.reInit()
  }
  onChange (value, selectedOptions) {
    // console.log(value, selectedOptions)
    const targetCode = selectedOptions[selectedOptions.length - 1]
    if (targetCode) {
      this.setState({
        outCode: targetCode.outCode,
      })
    }
  }
  reInit () {
    const value = this.props.form.getFieldsValue([this.props.province.key])
    if (!value[this.props.province.key]) {
      this.setState({
        outCode: '',
      })
    }
  }
  init () {
    const addressData = []
    for (let item of address01) {
      if (item.id !== 0) {
        item.value = item.id
        item.label = item.areaName
        addressData.push(item)
      }
    }
    const data = arrayToTree(addressData, 'id', 'parentId', 'children')
    this.setState({
      options: data,
    })
  }

  render () {
    const { province, form: { getFieldDecorator }, formItemLayout } = this.props
    return (
      <div>
        <FormItem label={province.label} {...formItemLayout} >
          {getFieldDecorator(province.key, {
            initialValue: this.props.updateData.length > 0 ? this.props.updateData : '',
            rules: [
              {
                required: province.required,
                message: '请选择区域地址',
              },
            ],
          })(
            <Cascader
              options={this.state.options}
              onChange={this.onChange.bind(this)}
              {...this.props}
              style={{ width: '100%' }}
            />
          )}
        </FormItem>
        <FormItem style={{ display: 'none' }}>
          {getFieldDecorator(province.outKey, {
            initialValue: this.state.outCode,
          })(
            <Input />
          )}
        </FormItem>
      </div>
    )
  }
}

AreaSelect.propTypes = {
  province: PropTypes.object,
  form: PropTypes.object,
  city: PropTypes.object,
  area: PropTypes.object,
  TypeChange: PropTypes.func,
  formItemLayout: PropTypes.obj,
  updateData: PropTypes.array,
  code: PropTypes.string,
}

export default AreaSelect
