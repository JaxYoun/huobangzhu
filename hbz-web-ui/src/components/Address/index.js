
import React from 'react'
import PropTypes from 'prop-types'
import { Input, Form, message } from 'antd'
import { Map, Marker, ScaleControl, NavigationControl } from 'react-bmap'
const FormItem = Form.Item
import styles from './index.less'

class Address extends React.Component {
  constructor (props) {
    super(props)
    const { address = {} } = props
    this.state = {
      key: address.key,
      lng: '',
      lat: '',
      zoom: '13',
    }
  }

  componentDidMount () {
    const { address = {} } = this.props
    // if (address.value) {
    //   this.setPoints(address.value)
    // }
    this.loadMap()
    this.setPoints(address.value)
  }
  componentWillReceiveProps (nextProps) {
    if (nextProps.lng.value) {
      this.setState({
        lng: nextProps.lng.value,
        lat: nextProps.lat.value,
      })
    }
  }
  onBlur = () => {
    const { address = {}, form: { getFieldValue } } = this.props
    setTimeout(() => {
      const value = getFieldValue(address.key)
      this.setPoints(value)
    })
  }
  onFocus = () => {
    this.loadMap()
  }
  setPoints = (value) => {
    if (!value) {
      return
    }
    const { lng = {}, lat = {}, address = {}, form: { setFieldsValue, resetFields } } = this.props
    const { BMap } = window
    const myGeo = new BMap.Geocoder()
    const changeValue = {}
    myGeo.getPoint(value, (point) => {
      if (!point) {
        message.warning('请输入合法地址')
        resetFields([address.key])
        return
      }
      changeValue[lng.key] = point.lng
      changeValue[lat.key] = point.lat
      this.setState({
        lng: point.lng,
        lat: point.lat,
        zoom: '14',
      })
      setFieldsValue(changeValue)
    })
  }
  loadMap = () => {
    const { address: { key }, form: { setFieldsValue } } = this.props
    const changeValue = {}
    const { BMap } = window
    const ac = new BMap.Autocomplete({ input: key }) // 建立一个自动完成的对象
    // 将地址解析结果显示在地图上,并调整地图视野
    ac.addEventListener('onconfirm', (e) => {    //  鼠标点击下拉列表后的事件
      const _value = e.item.value
      const value = `${_value.province}${_value.city}${_value.district}${_value.street}${_value.business}`

      changeValue[key] = value
      setFieldsValue(changeValue)
      this.setPoints(value)
    })
  }

  render () {
    // console.log(this.props.lng.value, this.props.lat.value, 'this.props.lat.value')
    const { address = {}, lng = {}, lat = {}, label = '地址', form: { getFieldDecorator }, map } = this.props
    return (<div>
      <FormItem label={label} >
        {getFieldDecorator(address.key, {
          rules: [
            {
              required: !this.props.requir,
              message: `请填写${label}`,
            },
          ],
          initialValue: address.value,
        })(
          <Input {...this.props} onBlur={this.onBlur} disabled={this.props.disabled} />
        )}
      </FormItem>
      <FormItem >
        {getFieldDecorator(lng.key, {
          initialValue: lng.value,
        })(
          <Input type="hidden" />
        )}
      </FormItem>
      <FormItem >
        {getFieldDecorator(lat.key, {
          initialValue: lat.value,
        })(
          <Input type="hidden" />
        )}
      </FormItem>
      {
        map ? (
          <Map center={{ lng: this.state.lng || '104.067923', lat: this.state.lat || '30.679943' }} zoom={this.state.zoom}>
            <Marker
              position={{ lng: this.state.lng, lat: this.state.lat }}
              icon="loc_red"
            />
            <ScaleControl />
            <NavigationControl />
          </Map>
        ) : null
      }
      {/* <div id="map" style={{ width: '100%', height: '300px' }}></div> */}
    </div>)
  }
}

Address.propTypes = {
  value: PropTypes.string,
  label: PropTypes.string,
  onChange: PropTypes.func,
  form: PropTypes.object,
  address: PropTypes.object,
  lng: PropTypes.object,
  lat: PropTypes.object,
  requir: PropTypes.bool,
  map: PropTypes.bool,
  disabled: PropTypes.bool,
}
export default Address
