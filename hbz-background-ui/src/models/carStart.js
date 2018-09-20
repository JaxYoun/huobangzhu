import { getData, getCarDatas, getDatas, addBatchStartVehicle, driverConfirmedPage, selectAllStartVehicleInformation,
  updateTransitState, updateShippingStatus, midwayLoading, addNewBatchStartVehicle } from '../services/carStart'
import { message } from 'antd'
export default {
  namespace: 'carStart',
  state: {
    fields: [],
    tableTime: 0,
    carTypeData: [],
    transitStateData: [],
    collapse: false,
    firmModal: {
      visible: false,
      firmData: {},
      loading: false,
    },
    // 新建单
    transportOrder: {
      fields: [],
      tableTime: 0,
      chooseData: {},
    },
    car: {
      firmData: {},
      fields: [],
      tableTime: 0,
      carData: [],
      chooseCar: {},
      dataSource: [
        {
          trackingNumber: '合计',
          waybillQuantity: 0,
          weight: 0,
          volume: 0,
          onCollection: 0,
          advancePayment: 0,
          alreadyPay: 0,
          putPay: 0,
          backPay: 0,
        },
      ],
      alreadyChooseData: [],
      selectedRowKeys: [],
    },
    chooseModal: {
      visible: false,
      firmData: {},
      loading: false,
    },
    unchooseModal: {
      visible: false,
      firmData: {},
      loading: false,
      id: null,
    },
    sureModal: {
      visible: false,
      firmData: {},
      loading: false,
      dataSource: [
        {
          trackingNumber: '合计',
          waybillQuantity: 0,
          weight: 0,
          volume: 0,
          onCollection: 0,
          advancePayment: 0,
          alreadyPay: 0,
          putPay: 0,
          backPay: 0,
        },
      ],
    },
    // 中途装车
    halfwayLoadModal: {
      visible: false,
      firmData: {},
      loading: false,
    },
    transportOrderHalfway: {
      fields: [],
      tableTime: 0,
      chooseData: {},
    },
    carHalfway: {
      fields: [],
      tableTime: 0,
      firmData: {},
      chooseCar: {},
      carData: [],
      trackingNumberAll: [],
      dataSource: [
        {
          trackingNumber: '合计',
          waybillQuantity: 0,
          weight: 0,
          volume: 0,
          onCollection: 0,
          advancePayment: 0,
          alreadyPay: 0,
          putPay: 0,
          backPay: 0,
        },
      ],
      alreadyChooseData: [],
      selectedRowKeys: [],
    },
    statusModal: {
      firmData: {},
      visible: false,
      loading: false,
    },
    chooseHalfModal: {
      visible: false,
      firmData: {},
      loading: false,
    },
    unchooseHalfModal: {
      visible: false,
      firmData: {},
      loading: false,
      id: null,
    },
    // 中途卸货
    halfwayunLoadModal: {
      visible: false,
      firmData: {},
      loading: false,
    },
    carHalfwayUnLoad: {
      fields: [],
      tableTime: 0,
      firmData: {},
      chooseCar: {},
      carData: [],
      trackingNumberAll: [],
      dataSource: [
        {
          trackingNumber: '合计',
          waybillQuantity: 0,
          weight: 0,
          volume: 0,
          onCollection: 0,
          advancePayment: 0,
          alreadyPay: 0,
          putPay: 0,
          backPay: 0,
        },
      ],
      alreadyChooseData: [],
      selectedRowKeys: [],
    },
    unLoadList: {
      fields: [],
      tableTime: 0,
      firmData: {},
      chooseData: {},
      carData: [],
      dataSource: [],
      alreadyChooseData: [],
      selectedRowKeys: [],
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/logistics/loadGoods/carStart') {
          dispatch({
            type: 'update',
            payload: {
              fields: [],
              tableTime: 0,
              carTypeData: [],
              transitStateData: [],
              collapse: false,
              firmModal: {
                visible: false,
                firmData: {},
                loading: false,
              },
              transportOrder: {
                fields: [],
                tableTime: 0,
                chooseData: {},
              },
              car: {
                firmData: {},
                fields: [],
                tableTime: 0,
                carData: [],
                chooseCar: {},
                dataSource: [
                  {
                    trackingNumber: '合计',
                    waybillQuantity: 0,
                    weight: 0,
                    volume: 0,
                    onCollection: 0,
                    advancePayment: 0,
                    alreadyPay: 0,
                    putPay: 0,
                    backPay: 0,
                  },
                ],
                alreadyChooseData: [],
                selectedRowKeys: [],
              },
              chooseModal: {
                visible: false,
                firmData: {},
                loading: false,
              },
              unchooseModal: {
                visible: false,
                firmData: {},
                loading: false,
                id: null,
              },
              sureModal: {
                visible: false,
                firmData: {},
                loading: false,
                dataSource: [
                  {
                    trackingNumber: '合计',
                    waybillQuantity: 0,
                    weight: 0,
                    volume: 0,
                    onCollection: 0,
                    advancePayment: 0,
                    alreadyPay: 0,
                    putPay: 0,
                    backPay: 0,
                  },
                ],
              },
               // 中途装车
              halfwayLoadModal: {
                visible: false,
                firmData: {},
                loading: false,
              },
              transportOrderHalfway: {
                fields: [],
                tableTime: 0,
                chooseData: {},
              },
              carHalfway: {
                fields: [],
                tableTime: 0,
                carData: [],
                firmData: {},
                chooseCar: {},
                trackingNumberAll: [],
                dataSource: [
                  {
                    trackingNumber: '合计',
                    waybillQuantity: 0,
                    weight: 0,
                    volume: 0,
                    onCollection: 0,
                    advancePayment: 0,
                    alreadyPay: 0,
                    putPay: 0,
                    backPay: 0,
                  },
                ],
                alreadyChooseData: [],
                selectedRowKeys: [],
              },
              statusModal: {
                firmData: {},
                visible: false,
                loading: false,
              },
              chooseHalfModal: {
                visible: false,
                firmData: {},
                loading: false,
              },
              unchooseHalfModal: {
                visible: false,
                firmData: {},
                loading: false,
                id: null,
              },
              // 中途卸货
              halfwayunLoadModal: {
                visible: false,
                firmData: {},
                loading: false,
              },
              carHalfwayUnLoad: {
                fields: [],
                tableTime: 0,
                firmData: {},
                chooseCar: {},
                carData: [],
                trackingNumberAll: [],
                dataSource: [
                  {
                    trackingNumber: '合计',
                    waybillQuantity: 0,
                    weight: 0,
                    volume: 0,
                    onCollection: 0,
                    advancePayment: 0,
                    alreadyPay: 0,
                    putPay: 0,
                    backPay: 0,
                  },
                ],
                alreadyChooseData: [],
                selectedRowKeys: [],
              },
              unLoadList: {
                fields: [],
                tableTime: 0,
                firmData: {},
                chooseData: {},
                carData: [],
                dataSource: [],
                alreadyChooseData: [],
                selectedRowKeys: [],
              },
            },
          })
          dispatch({
            type: 'getCarTypeData',
            payload: { type: 'VehicleType' },
          })
          dispatch({
            type: 'getTransitStateData',
            payload: { enumname: 'TransitState' },
          })
          dispatch({
            type: 'getCarData',
          })
        }
      })
    },
  },
  effects: {
    * update ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload,
      })
    },
    * updatefirmModal ({ payload }, { put }) {
      yield put({
        type: 'updatefirmModalStates',
        payload,
      })
    },
    * updateTransportOrder ({ payload }, { put }) {
      yield put({
        type: 'updateTransportOrderStates',
        payload,
      })
    },
    * updateCar ({ payload }, { put }) {
      yield put({
        type: 'updateCarStates',
        payload,
      })
    },
    * updateChooseModal ({ payload }, { put }) {
      yield put({
        type: 'updateChooseModalStates',
        payload,
      })
    },
    * updateunChooseModal ({ payload }, { put }) {
      yield put({
        type: 'updateunChooseModalStates',
        payload,
      })
    },
    * updateSureModal ({ payload }, { put }) {
      yield put({
        type: 'updateSureModalStates',
        payload,
      })
    },
    // 中途装车卸货
    * updateHalfwayLoadModal ({ payload }, { put }) {
      yield put({
        type: 'updateHalfwayLoadModalStates',
        payload,
      })
    },
    * updatetransportOrderHalfway ({ payload }, { put }) {
      yield put({
        type: 'updatetransportOrderHalfwayStates',
        payload,
      })
    },
    * updateCarHalfway ({ payload }, { put }) {
      yield put({
        type: 'updateCarHalfwayStates',
        payload,
      })
    },
    * updateStatusModal ({ payload }, { put }) {
      yield put({
        type: 'updateStatusModalStates',
        payload,
      })
    },
    * updateChooseHalfModal ({ payload }, { put }) {
      yield put({
        type: 'updateChooseHalfModalStates',
        payload,
      })
    },
    * updateunChooseHalfModal ({ payload }, { put }) {
      yield put({
        type: 'updateunChooseHalfModalStates',
        payload,
      })
    },
    // 卸货
    * updateHalfwayunLoadModal ({ payload }, { put }) {
      yield put({
        type: 'updateHalfwayunLoadModalStates',
        payload,
      })
    },
    * updateCarHalfwayUnLoad ({ payload }, { put }) {
      yield put({
        type: 'updateCarHalfwayUnLoadStates',
        payload,
      })
    },
    * updateunLoadList ({ payload }, { put }) {
      yield put({
        type: 'updateunLoadListStates',
        payload,
      })
    },
    // 获取车辆类型
    * getCarTypeData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          carTypeData: res.data,
        },
      })
    },
    // 获取在途状态
    * getTransitStateData ({ payload }, { put, call }) {
      const res = yield call(getDatas, payload)
      yield put({
        type: 'update',
        payload: {
          transitStateData: res.data,
        },
      })
    },
    // 获取车辆信息
    * getCarData ({ payload }, { put, call }) {
      const res = yield call(getCarDatas, payload)
      yield put({
        type: 'updateCar',
        payload: {
          carData: res.data,
        },
      })
    },
    // 装车的方法
    * uploadGoods ({ payload }, { put, select }) {
      // 添加的数据
      let a = yield select(state => state.carStart)
      a = a.transportOrder.chooseData
      // 判断装车数量
      if (a[0].inventoryQuantity < payload.waybillQuantity) {
        message.error('装车数量不能大于库存数量')
      } else {
        a = [{ ...payload, ...a[0] }]
        let alreadyPay = 0
        let putPay = 0
        let backPay = 0
      // 三种付款方式数据处理
        if (a[0].paymentMethod === '0') {
          alreadyPay = ((a[0].fotalFee / a[0].amount) * a[0].waybillQuantity).toFixed(2)
          putPay = 0
          backPay = 0
        } else if (a[0].paymentMethod === '1') {
          putPay = ((a[0].fotalFee / a[0].amount) * a[0].waybillQuantity).toFixed(2)
          alreadyPay = 0
          backPay = 0
        } else if (a[0].paymentMethod === '2') {
          backPay = ((a[0].fotalFee / a[0].amount) * a[0].waybillQuantity).toFixed(2)
          alreadyPay = 0
          putPay = 0
        } else {
          backPay = 0
          alreadyPay = 0
          putPay = 0
        }
        let cargoInformationId = { cargoInformationId: a[0].id,
          alreadyPay,
          backPay,
          putPay,
        }
        a = [{ ...cargoInformationId, ...a[0] }]
        // 已有的数据
        let b = yield select(state => state.carStart)
        b = b.car.dataSource
        // 判断是否重复
        let add = true
        for (let key of b) {
          if (key.id === a[0].id) {
            add = false
          }
        }
        if (add) {
          yield put({
            type: 'updateChooseModal',
            payload: {
              visible: false,
            },
          })
          // 计算合计数据
          let waybillQuantity = 0
          let weight = 0
          let volume = 0
          let onCollection = 0
          let advancePayment = 0
          alreadyPay = 0
          putPay = 0
          backPay = 0
          for (let key of b) {
            if (key.trackingNumber !== '合计') {
              waybillQuantity = key.waybillQuantity + waybillQuantity
              weight = key.singleWeight * key.waybillQuantity + weight
              volume = key.singleVolume * key.waybillQuantity + volume
              onCollection = key.onCollection + onCollection
              advancePayment = key.advancePayment + advancePayment
              alreadyPay = Number(key.alreadyPay) + alreadyPay
              putPay = Number(key.putPay) + putPay
              backPay = Number(key.backPay) + backPay
            }
          }
          b[b.length - 1].waybillQuantity = waybillQuantity + a[0].waybillQuantity
          b[b.length - 1].weight = weight + a[0].singleWeight * a[0].waybillQuantity
          b[b.length - 1].volume = volume + a[0].singleVolume * a[0].waybillQuantity
          b[b.length - 1].onCollection = onCollection + a[0].onCollection
          b[b.length - 1].advancePayment = advancePayment + a[0].advancePayment
          b[b.length - 1].alreadyPay = alreadyPay + Number(a[0].alreadyPay)
          b[b.length - 1].putPay = putPay + Number(a[0].putPay)
          b[b.length - 1].backPay = backPay + Number(a[0].backPay)
          yield put({
            type: 'updateCar',
            payload: {
              dataSource: [...a, ...b],
            },
          })
        } else {
          message.error('同一运单数据只能装车一次')
        }
      }
      yield put({
        type: 'updateChooseModal',
        payload: {
          loading: false,
        },
      })
    },
    // 取消的方法
    * unloadGoods ({ payload }, { put, select }) {
    // 减少的数据
      let a = yield select(state => state.carStart)
      a = a.car.alreadyChooseData
      let b = yield select(state => state.carStart)
      b = b.car.dataSource
      // 判断取消数量
      if (a[0].waybillQuantity < payload.cancellations) {
        message.error('取消数量不能大于装车数量')
      } else {
        if (a[0].waybillQuantity === payload.cancellations) {
          b.splice(b.findIndex(item => item.id === a[0].id), 1)
          yield put({
            type: 'updateCar',
            payload: {
              selectedRowKeys: [],
            },
          })
          yield put({
            type: 'updateunChooseModal',
            payload: {
              visible: false,
            },
          })
        }
        a[0].waybillQuantity = a[0].waybillQuantity - payload.cancellations
        a[0].alreadyPay = a[0].alreadyPay - (a[0].fotalFee / a[0].amount) * payload.cancellations < 0 ? 0 : a[0].alreadyPay - (a[0].fotalFee / a[0].amount) * payload.cancellations
        a[0].putPay = a[0].putPay - (a[0].fotalFee / a[0].amount) * payload.cancellations < 0 ? 0 : a[0].putPay - (a[0].fotalFee / a[0].amount) * payload.cancellations
        a[0].backPay = a[0].backPay - (a[0].fotalFee / a[0].amount) * payload.cancellations < 0 ? 0 : a[0].backPay - (a[0].fotalFee / a[0].amount) * payload.cancellations
        yield put({
          type: 'updateCar',
          payload: {
            ...a,
          },
        })
        yield put({
          type: 'updateunChooseModal',
          payload: {
            visible: false,
          },
        })
        // 计算合计数据
        let waybillQuantity = 0
        let weight = 0
        let volume = 0
        let onCollection = 0
        let advancePayment = 0
        let alreadyPay = 0
        let putPay = 0
        let backPay = 0

        for (let key of b) {
          if (key.trackingNumber !== '合计') {
            waybillQuantity = key.waybillQuantity + waybillQuantity
            weight = key.singleWeight * key.waybillQuantity + weight
            volume = key.singleVolume * key.waybillQuantity + volume
            onCollection = key.onCollection + onCollection
            advancePayment = key.advancePayment + advancePayment
            alreadyPay = key.alreadyPay + alreadyPay
            putPay = key.putPay + putPay
            backPay = key.backPay + backPay
          }
        }
        b[b.length - 1].waybillQuantity = waybillQuantity
        b[b.length - 1].weight = weight
        b[b.length - 1].volume = volume
        b[b.length - 1].onCollection = onCollection
        b[b.length - 1].advancePayment = advancePayment
        b[b.length - 1].alreadyPay = alreadyPay
        b[b.length - 1].putPay = putPay
        b[b.length - 1].backPay = backPay
        yield put({
          type: 'updateCar',
          payload: {
            ...b,
          },
        })
      }
      yield put({
        type: 'updateunChooseModal',
        payload: {
          loading: false,
        },
      })
    },
    // 保存发车单
    * saveCar ({ payload }, { put, call, select }) {
      let addData = yield select(state => state.carStart)
      addData = addData.car.dataSource
      let data = {}
      // 删掉合计数据
      addData.pop()
      if (addData.length < 1) {
        message.error('请选择货物')
      } else {
        data = { ...payload.data, ...data }
        data = { startVehicleDTOS: addData, ...data }
        const res = yield call(addBatchStartVehicle, data)
        if (res.code === '200') {
          yield put({
            type: 'cleanModal',
            payload: {},
          })
          yield put({
            type: 'update',
            payload: {
              tableTime: new Date().getTime(),
            },
          })
        }
      }
    },
    // 清空并关闭新建框
    * cleanModal ({ payload }, { put, call }) {
      yield put({
        type: 'updatefirmModal',
        payload: {
          visible: false,
          firmData: {},
          loading: false,
        },
      })
      yield put({
        type: 'updateTransportOrder',
        payload: {
          fields: [],
          tableTime: 0,
          chooseData: [],
        },
      })
      yield put({
        type: 'updateCar',
        payload: {
          firmData: {},
          fields: [],
          tableTime: 0,
          chooseCar: {},
          dataSource: [
            {
              trackingNumber: '合计',
              waybillQuantity: 0,
              weight: 0,
              volume: 0,
              onCollection: 0,
              advancePayment: 0,
              alreadyPay: 0,
              putPay: 0,
              backPay: 0,
            },
          ],
          alreadyChooseData: [],
          selectedRowKeys: [],
        },
      })
    },
    // 清空并关闭中途发车
    * cleanHalfModal ({ payload }, { put, call }) {
      yield put({
        type: 'updateHalfwayLoadModal',
        payload: {
          visible: false,
          firmData: {},
          loading: false,
        },
      })
      yield put({
        type: 'updatetransportOrderHalfway',
        payload: {
          fields: [],
          tableTime: 0,
          chooseData: [],
        },
      })
      yield put({
        type: 'updateCarHalfway',
        payload: {
          fields: [],
          tableTime: 0,
          chooseCar: {},
          dataSource: [
            {
              trackingNumber: '合计',
              waybillQuantity: 0,
              weight: 0,
              volume: 0,
              onCollection: 0,
              advancePayment: 0,
            },
          ],
          alreadyChooseData: [],
          selectedRowKeys: [],
        },
      })
    },
    // 获取确认信息
    * getSureData ({ payload }, { put, call, select }) {
      const res = yield call(selectAllStartVehicleInformation, payload)
      if (res.code === '200') {
        let a = yield select(state => state.carStart)
        a = a.sureModal.dataSource
        let trackingNumberAll = []
        let waybillQuantity = 0
        let weight = 0
        let volume = 0
        let onCollection = 0
        let advancePayment = 0
        let alreadyPay = 0
        let putPay = 0
        let backPay = 0
        for (let key of res.data.startVehicleDTOS) {
          trackingNumberAll.push(key.trackingNumber)
          if (key.paymentMethod === '0') {
            key.alreadyPay = ((key.fotalFee / key.amount) * key.waybillQuantity).toFixed(2)
            key.putPay = 0
            key.backPay = 0
          } else if (key.paymentMethod === '1') {
            key.putPay = ((key.fotalFee / key.amount) * key.waybillQuantity).toFixed(2)
            key.alreadyPay = 0
            key.backPay = 0
          } else if (key.paymentMethod === '2') {
            key.backPay = ((key.fotalFee / key.amount) * key.waybillQuantity).toFixed(2)
            key.alreadyPay = 0
            key.putPay = 0
          } else {
            key.backPay = 0
            key.alreadyPay = 0
            key.putPay = 0
          }
          waybillQuantity = waybillQuantity + key.waybillQuantity
          weight = weight + key.singleWeight * key.waybillQuantity
          volume = volume + key.singleVolume * key.waybillQuantity
          onCollection = onCollection + key.onCollection
          advancePayment = advancePayment + key.advancePayment
          alreadyPay = Number(alreadyPay) + Number(key.alreadyPay)
          putPay = putPay + key.putPay
          backPay = backPay + key.backPay
        }
        // 中途装车计算合计数据
        a[0].waybillQuantity = waybillQuantity
        a[0].weight = weight
        a[0].volume = volume
        a[0].onCollection = onCollection
        a[0].advancePayment = advancePayment
        a[0].alreadyPay = alreadyPay
        a[0].putPay = putPay
        a[0].backPay = backPay
        yield put({
          type: 'updateSureModal',
          payload: {
            firmData: res.data,
            dataSource: [...res.data.startVehicleDTOS, ...a],
          },
        })
      }
    },
    // 中途装车数据获取
    * halfwayLoadData ({ payload }, { put, call, select }) {
      const res = yield call(selectAllStartVehicleInformation, payload)
      if (res.code === '200') {
        let a = yield select(state => state.carStart)
        a = a.carHalfway.dataSource
        let trackingNumberAll = []
        let waybillQuantity = 0
        let weight = 0
        let volume = 0
        let onCollection = 0
        let advancePayment = 0
        let alreadyPay = 0
        let putPay = 0
        let backPay = 0
        for (let key of res.data.startVehicleDTOS) {
          trackingNumberAll.push(key.trackingNumber)
          if (key.paymentMethod === '0') {
            key.alreadyPay = ((key.fotalFee / key.amount) * key.waybillQuantity).toFixed(2)
            key.putPay = 0
            key.backPay = 0
          } else if (key.paymentMethod === '1') {
            key.putPay = ((key.fotalFee / key.amount) * key.waybillQuantity).toFixed(2)
            key.alreadyPay = 0
            key.backPay = 0
          } else if (key.paymentMethod === '2') {
            key.backPay = ((key.fotalFee / key.amount) * key.waybillQuantity).toFixed(2)
            key.alreadyPay = 0
            key.putPay = 0
          } else {
            key.backPay = 0
            key.alreadyPay = 0
            key.putPay = 0
          }
          waybillQuantity = waybillQuantity + key.waybillQuantity
          weight = weight + key.singleWeight * key.waybillQuantity
          volume = volume + key.singleVolume * key.waybillQuantity
          onCollection = onCollection + key.onCollection
          advancePayment = advancePayment + key.advancePayment
          alreadyPay = Number(alreadyPay) + Number(key.alreadyPay)
          putPay = putPay + key.putPay
          backPay = backPay + key.backPay
        }
        // 中途装车计算合计数据
        a[0].waybillQuantity = waybillQuantity
        a[0].weight = weight
        a[0].volume = volume
        a[0].onCollection = onCollection
        a[0].advancePayment = advancePayment
        a[0].alreadyPay = alreadyPay
        a[0].putPay = putPay
        a[0].backPay = backPay
        yield put({
          type: 'updateCarHalfway',
          payload: {
            firmData: res.data,
            chooseCar: res.data.vehicleInformationDTO,
            dataSource: [...res.data.startVehicleDTOS, ...a],
            trackingNumberAll,
          },
        })
      }
    },
    // 编辑状态
    * updateStatus ({ payload }, { put, call }) {
      const res = yield call(updateTransitState, payload)
      if (res.code === '200') {
        yield put({
          type: 'updateStatusModal',
          payload: {
            firmData: {},
            visible: false,
            loading: false,
          },
        })
        yield put({
          type: 'update',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      }
    },
    // 关闭编辑状态模态框
    * cleanStatusModal ({ payload }, { put, call }) {
      yield put({
        type: 'updateStatusModal',
        payload: {
          firmData: {},
          loading: false,
          visible: false,
        },
      })
    },
    // 关闭确认发车模态框
    * cleanSureModal ({ payload }, { put, call }) {
      yield put({
        type: 'updateSureModal',
        payload: {
          firmData: {},
          loading: false,
          visible: false,
          dataSource: [
            {
              trackingNumber: '合计',
              waybillQuantity: 0,
              weight: 0,
              volume: 0,
              onCollection: 0,
              advancePayment: 0,
              alreadyPay: 0,
              putPay: 0,
              backPay: 0,
            },
          ],
        },
      })
    },
    // 确认发车
    * sure ({ payload }, { put, call }) {
      const res = yield call(updateShippingStatus, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanSureModal',
        })
        yield put({
          type: 'update',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      }
    },
    // 中途装车的方法
    * uploadHalfwayGoods ({ payload }, { put, select }) {
      // 添加的数据
      let a = yield select(state => state.carStart)
      a = a.transportOrderHalfway.chooseData
      // 判断装车数量
      if (a[0].inventoryQuantity < payload.waybillQuantity) {
        message.error('装车数量不能大于库存数量')
      } else {
        a = [{ ...payload, ...a[0] }]
        let alreadyPay = 0
        let putPay = 0
        let backPay = 0
      // 三种付款方式数据处理
        if (a[0].paymentMethod === '0') {
          alreadyPay = ((a[0].fotalFee / a[0].amount) * a[0].waybillQuantity).toFixed(2)
          putPay = 0
          backPay = 0
        } else if (a[0].paymentMethod === '1') {
          putPay = ((a[0].fotalFee / a[0].amount) * a[0].waybillQuantity).toFixed(2)
          alreadyPay = 0
          backPay = 0
        } else if (a[0].paymentMethod === '2') {
          backPay = ((a[0].fotalFee / a[0].amount) * a[0].waybillQuantity).toFixed(2)
          alreadyPay = 0
          putPay = 0
        } else {
          backPay = 0
          alreadyPay = 0
          putPay = 0
        }
        let cargoInformationId = { cargoInformationId: a[0].id,
          alreadyPay,
          backPay,
          putPay,
        }
        a = [{ ...cargoInformationId, ...a[0] }]
        console.log('a1',a)
        // 已有的数据
        let b = yield select(state => state.carStart)
        let c = b.carHalfway.trackingNumberAll
        b = b.carHalfway.dataSource
        // 判断是否重复
        let add = true
        for (let key of b) {
          if (key.id === a[0].id) {
            add = false
          }
        }
        if (add) {
          yield put({
            type: 'updateChooseHalfModal',
            payload: {
              visible: false,
            },
          })
          // 计算合计数据
          let waybillQuantity = 0
          let weight = 0
          let volume = 0
          let onCollection = 0
          let advancePayment = 0
          alreadyPay = 0
          putPay = 0
          backPay = 0
          for (let key of b) {
            if (key.trackingNumber !== '合计') {
              waybillQuantity = key.waybillQuantity + waybillQuantity
              weight = key.singleWeight * key.waybillQuantity + weight
              volume = key.singleVolume * key.waybillQuantity + volume
              onCollection = key.onCollection + onCollection
              advancePayment = key.advancePayment + advancePayment
              alreadyPay = Number(key.alreadyPay) + alreadyPay
              putPay = Number(key.putPay) + putPay
              backPay = Number(key.backPay) + backPay
            }
          }
          b[b.length - 1].waybillQuantity = waybillQuantity + a[0].waybillQuantity
          b[b.length - 1].weight = weight + a[0].singleWeight * a[0].waybillQuantity
          b[b.length - 1].volume = volume + a[0].singleVolume * a[0].waybillQuantity
          b[b.length - 1].onCollection = onCollection + a[0].onCollection
          b[b.length - 1].advancePayment = advancePayment + a[0].advancePayment
          b[b.length - 1].alreadyPay = alreadyPay + Number(a[0].alreadyPay)
          b[b.length - 1].putPay = putPay + Number(a[0].putPay)
          b[b.length - 1].backPay = backPay + Number(a[0].backPay)
          yield put({
            type: 'updateCarHalfway',
            payload: {
              dataSource: [...a, ...b],
            },
          })
        } else {
          message.error('同一运单数据只能装车一次')
        }
      }
      yield put({
        type: 'updateChooseHalfModal',
        payload: {
          loading: false,
        },
      })
    },
    // 中途装车取消的方法
    * unloadHalfwayGoods ({ payload }, { put, select }) {
    // 减少的数据
      let a = yield select(state => state.carStart)
      a = a.carHalfway.alreadyChooseData
      let b = yield select(state => state.carStart)
      b = b.carHalfway.dataSource
      // 判断取消数量
      if (a[0].waybillQuantity < payload.cancellations) {
        message.error('取消数量不能大于装车数量')
      } else {
        if (a[0].waybillQuantity === payload.cancellations) {
          b.splice(b.findIndex(item => item.id === a[0].id), 1)
          yield put({
            type: 'updateCarHalfway',
            payload: {
              selectedRowKeys: [],
            },
          })
          yield put({
            type: 'updateunChooseHalfModal',
            payload: {
              visible: false,
            },
          })
        }
        a[0].waybillQuantity = a[0].waybillQuantity - payload.cancellations
        a[0].alreadyPay = a[0].alreadyPay - (a[0].fotalFee / a[0].amount) * payload.cancellations < 0 ? 0 : a[0].alreadyPay - (a[0].fotalFee / a[0].amount) * payload.cancellations
        a[0].putPay = a[0].putPay - (a[0].fotalFee / a[0].amount) * payload.cancellations < 0 ? 0 : a[0].putPay - (a[0].fotalFee / a[0].amount) * payload.cancellations
        a[0].backPay = a[0].backPay - (a[0].fotalFee / a[0].amount) * payload.cancellations < 0 ? 0 : a[0].backPay - (a[0].fotalFee / a[0].amount) * payload.cancellations
        yield put({
          type: 'updateCarHalfway',
          payload: {
            ...a,
          },
        })
        yield put({
          type: 'updateunChooseHalfModal',
          payload: {
            visible: false,
          },
        })
        // 计算合计数据
        let waybillQuantity = 0
        let weight = 0
        let volume = 0
        let onCollection = 0
        let advancePayment = 0
        let alreadyPay = 0
        let putPay = 0
        let backPay = 0

        for (let key of b) {
          if (key.trackingNumber !== '合计') {
            waybillQuantity = key.waybillQuantity + waybillQuantity
            weight = key.singleWeight * key.waybillQuantity + weight
            volume = key.singleVolume * key.waybillQuantity + volume
            onCollection = key.onCollection + onCollection
            advancePayment = key.advancePayment + advancePayment
            alreadyPay = key.alreadyPay + alreadyPay
            putPay = key.putPay + putPay
            backPay = key.backPay + backPay
          }
        }
        b[b.length - 1].waybillQuantity = waybillQuantity
        b[b.length - 1].weight = weight
        b[b.length - 1].volume = volume
        b[b.length - 1].onCollection = onCollection
        b[b.length - 1].advancePayment = advancePayment
        b[b.length - 1].alreadyPay = alreadyPay
        b[b.length - 1].putPay = putPay
        b[b.length - 1].backPay = backPay
        yield put({
          type: 'updateCarHalfway',
          payload: {
            ...b,
          },
        })
      }
      yield put({
        type: 'updateunChooseHalfModal',
        payload: {
          loading: false,
        },
      })
    },
    // 保存中途发车
    * saveHalfCar ({ payload }, { put, call, select }) {
      let addData = yield select(state => state.carStart)
      addData = addData.carHalfway.dataSource
      let data = {}
      // 删掉合计数据
      addData.pop()
      if (addData.length < 1) {
        message.error('请选择货物')
      } else {
        data = { ...payload.data, ...data }
        data = { startVehicleDTOS: addData, ...data }
        const res = yield call(midwayLoading, data)
        if (res.code === '200') {
          yield put({
            type: 'cleanHalfModal',
            payload: {},
          })
          yield put({
            type: 'update',
            payload: {
              tableTime: new Date().getTime(),
            },
          })
        }
      }
    },
    // 编辑新建单
    * updateNewModal ({ payload }, { put, call, select }) {
      const res = yield call(selectAllStartVehicleInformation, payload)
      if (res.code === '200') {
        let a = yield select(state => state.carStart)
        a = a.car.dataSource
        let trackingNumberAll = []
        let waybillQuantity = 0
        let weight = 0
        let volume = 0
        let onCollection = 0
        let advancePayment = 0
        let alreadyPay = 0
        let putPay = 0
        let backPay = 0
        for (let key of res.data.startVehicleDTOS) {
          if (key.paymentMethod === '0') {
            key.alreadyPay = ((key.fotalFee / key.amount) * key.waybillQuantity).toFixed(2)
            key.putPay = 0
            key.backPay = 0
          } else if (key.paymentMethod === '1') {
            key.putPay = ((key.fotalFee / key.amount) * key.waybillQuantity).toFixed(2)
            key.alreadyPay = 0
            key.backPay = 0
          } else if (key.paymentMethod === '2') {
            key.backPay = ((key.fotalFee / key.amount) * key.waybillQuantity).toFixed(2)
            key.alreadyPay = 0
            key.putPay = 0
          } else {
            key.backPay = 0
            key.alreadyPay = 0
            key.putPay = 0
          }
          waybillQuantity = waybillQuantity + key.waybillQuantity
          weight = weight + key.singleWeight * key.waybillQuantity
          volume = volume + key.singleVolume * key.waybillQuantity
          onCollection = onCollection + key.onCollection
          advancePayment = advancePayment + key.advancePayment
          alreadyPay = Number(alreadyPay) + Number(key.alreadyPay)
          putPay = putPay + key.putPay
          backPay = backPay + key.backPay
        }
        // 中途装车计算合计数据
        a[0].waybillQuantity = waybillQuantity
        a[0].weight = weight
        a[0].volume = volume
        a[0].onCollection = onCollection
        a[0].advancePayment = advancePayment
        a[0].alreadyPay = alreadyPay
        a[0].putPay = putPay
        a[0].backPay = backPay
        yield put({
          type: 'updateCar',
          payload: {
            firmData: res.data,
            chooseCar: res.data.vehicleInformationDTO,
            dataSource: [...res.data.startVehicleDTOS, ...a],
            trackingNumberAll,
          },
        })
      }
    },
    // 保存新建单编辑
    * updateAddCar ({ payload }, { put, call, select }) {
      let addData = yield select(state => state.carStart)
      addData = addData.car.dataSource
      let data = {}
      // 删掉合计数据
      addData.pop()
      if (addData.length < 1) {
        message.error('请选择货物')
      } else {
        data = { ...payload.data, ...data }
        data = { startVehicleDTOS: addData, ...data }
        const res = yield call(addNewBatchStartVehicle, data)
        if (res.code === '200') {
          yield put({
            type: 'cleanModal',
            payload: {},
          })
          yield put({
            type: 'update',
            payload: {
              tableTime: new Date().getTime(),
            },
          })
        }
      }
    },
    // 中途卸货数据获取
    * halfwayunLoadData ({ payload }, { put, call, select }) {
      const res = yield call(selectAllStartVehicleInformation, payload)
      if (res.code === '200') {
        let a = yield select(state => state.carStart)
        a = a.carHalfwayUnLoad.dataSource
        let waybillQuantity = 0
        let weight = 0
        let volume = 0
        let onCollection = 0
        let advancePayment = 0
        let alreadyPay = 0
        let putPay = 0
        let backPay = 0
        for (let key of res.data.startVehicleDTOS) {
          if (key.paymentMethod === '0') {
            key.alreadyPay = ((key.fotalFee / key.amount) * key.waybillQuantity).toFixed(2)
            key.putPay = 0
            key.backPay = 0
          } else if (key.paymentMethod === '1') {
            key.putPay = ((key.fotalFee / key.amount) * key.waybillQuantity).toFixed(2)
            key.alreadyPay = 0
            key.backPay = 0
          } else if (key.paymentMethod === '2') {
            key.backPay = ((key.fotalFee / key.amount) * key.waybillQuantity).toFixed(2)
            key.alreadyPay = 0
            key.putPay = 0
          } else {
            key.backPay = 0
            key.alreadyPay = 0
            key.putPay = 0
          }
          waybillQuantity = waybillQuantity + key.waybillQuantity
          weight = weight + key.singleWeight * key.waybillQuantity
          volume = volume + key.singleVolume * key.waybillQuantity
          onCollection = onCollection + key.onCollection
          advancePayment = advancePayment + key.advancePayment
          alreadyPay = Number(alreadyPay) + Number(key.alreadyPay)
          putPay = putPay + key.putPay
          backPay = backPay + key.backPay
        }
        // 中途装车计算合计数据
        a[0].waybillQuantity = waybillQuantity
        a[0].weight = weight
        a[0].volume = volume
        a[0].onCollection = onCollection
        a[0].advancePayment = advancePayment
        a[0].alreadyPay = alreadyPay
        a[0].putPay = putPay
        a[0].backPay = backPay
        yield put({
          type: 'updateCarHalfwayUnLoad',
          payload: {
            firmData: res.data,
            chooseCar: res.data.vehicleInformationDTO,
            dataSource: [...res.data.startVehicleDTOS, ...a],
          },
        })
      }
    },
    // 卸货添加数据
    * HalfwayGoodsUnLoad ({ payload }, { put, select }) {
      let a = yield select(state => state.carStart)
      a = a.carHalfwayUnLoad.alreadyChooseData
      let b = yield select(state => state.carStart)
      b = b.carHalfwayUnLoad.dataSource
      let c = yield select(state => state.carStart)
      c = c.unLoadList.dataSource
      b.splice(b.findIndex(item => item.id === a[0].id), 1)
      yield put({
        type: 'updateunLoadList',
        payload: {
          dataSource: [...c, ...a],
        },
      })
      // 计算合计数据
      let waybillQuantity = 0
      let weight = 0
      let volume = 0
      let onCollection = 0
      let advancePayment = 0
      let alreadyPay = 0
      let putPay = 0
      let backPay = 0
      for (let key of b) {
        if (key.trackingNumber !== '合计') {
          waybillQuantity = key.waybillQuantity + waybillQuantity
          weight = key.singleWeight * key.waybillQuantity + weight
          volume = key.singleVolume * key.waybillQuantity + volume
          onCollection = key.onCollection + onCollection
          advancePayment = key.advancePayment + advancePayment
          alreadyPay = key.alreadyPay + alreadyPay
          putPay = key.putPay + putPay
          backPay = key.backPay + backPay
        }
      }
      b[b.length - 1].waybillQuantity = waybillQuantity
      b[b.length - 1].weight = weight
      b[b.length - 1].volume = volume
      b[b.length - 1].onCollection = onCollection
      b[b.length - 1].advancePayment = advancePayment
      b[b.length - 1].alreadyPay = alreadyPay
      b[b.length - 1].putPay = putPay
      b[b.length - 1].backPay = backPay
      yield put({
        type: 'updateCarHalfwayUnLoad',
        payload: {
          ...b,
          selectedRowKeys: [],
        },
      })
    },
    // 重置
    * HalfwayGoodsReset ({ payload }, { put, call, select }) {
      yield put({
        type: 'updateunLoadList',
        payload: {
          fields: [],
          tableTime: 0,
          firmData: {},
          chooseData: {},
          carData: [],
          dataSource: [],
          alreadyChooseData: [],
          selectedRowKeys: [],
        },
      })
      yield put({
        type: 'updateCarHalfwayUnLoad',
        payload: {
          fields: [],
          tableTime: 0,
          carData: [],
          trackingNumberAll: [],
          dataSource: [
            {
              trackingNumber: '合计',
              waybillQuantity: 0,
              weight: 0,
              volume: 0,
              onCollection: 0,
              advancePayment: 0,
              alreadyPay: 0,
              putPay: 0,
              backPay: 0,
            },
          ],
          alreadyChooseData: [],
          selectedRowKeys: [],
        },
      })
      const res = yield call(selectAllStartVehicleInformation, payload)
      if (res.code === '200') {
        let a = yield select(state => state.carStart)
        a = a.carHalfwayUnLoad.dataSource
        let waybillQuantity = 0
        let weight = 0
        let volume = 0
        let onCollection = 0
        let advancePayment = 0
        let alreadyPay = 0
        let putPay = 0
        let backPay = 0
        for (let key of res.data.startVehicleDTOS) {
          if (key.paymentMethod === '0') {
            key.alreadyPay = ((key.fotalFee / key.amount) * key.waybillQuantity).toFixed(2)
            key.putPay = 0
            key.backPay = 0
          } else if (key.paymentMethod === '1') {
            key.putPay = ((key.fotalFee / key.amount) * key.waybillQuantity).toFixed(2)
            key.alreadyPay = 0
            key.backPay = 0
          } else if (key.paymentMethod === '2') {
            key.backPay = ((key.fotalFee / key.amount) * key.waybillQuantity).toFixed(2)
            key.alreadyPay = 0
            key.putPay = 0
          } else {
            key.backPay = 0
            key.alreadyPay = 0
            key.putPay = 0
          }
          waybillQuantity = waybillQuantity + key.waybillQuantity
          weight = weight + key.singleWeight * key.waybillQuantity
          volume = volume + key.singleVolume * key.waybillQuantity
          onCollection = onCollection + key.onCollection
          advancePayment = advancePayment + key.advancePayment
          alreadyPay = Number(alreadyPay) + Number(key.alreadyPay)
          putPay = putPay + key.putPay
          backPay = backPay + key.backPay
        }
        // 中途装车计算合计数据
        a[0].waybillQuantity = waybillQuantity
        a[0].weight = weight
        a[0].volume = volume
        a[0].onCollection = onCollection
        a[0].advancePayment = advancePayment
        a[0].alreadyPay = alreadyPay
        a[0].putPay = putPay
        a[0].backPay = backPay
        yield put({
          type: 'updateCarHalfwayUnLoad',
          payload: {
            dataSource: [...res.data.startVehicleDTOS, ...a],
          },
        })
      }
    },
    // 保存卸货
    * saveUnLoad ({ payload }, { put, call, select }) {
      let addData = yield select(state => state.carStart)
      addData = addData.unLoadList.dataSource
      let array = []
      for (let key of addData) {
        array.push(key.id)
      }
      console.log(array)
      // // 删掉合计数据
      // addData.pop()
      // if (addData.length < 1) {
      //   message.error('请选择货物')
      // } else {
      //   data = { ...payload.data, ...data }
      //   data = { startVehicleDTOS: addData, ...data }
      //   const res = yield call(midwayLoading, data)
      //   if (res.code === '200') {
      //     yield put({
      //       type: 'cleanHalfModal',
      //       payload: {},
      //     })
      //     yield put({
      //       type: 'update',
      //       payload: {
      //         tableTime: new Date().getTime(),
      //       },
      //     })
      //   }
      //}
    },
    // 关闭中途卸货模态框
    * cleanUnLoadModal ({ payload }, { put }) {
      yield put({
        type: 'updateHalfwayunLoadModal',
        payload: {
          visible: false,
          firmData: {},
          loading: false,
        },
      })
      yield put({
        type: 'updateCarHalfwayUnLoad',
        payload: {
          fields: [],
          tableTime: 0,
          firmData: {},
          chooseCar: {},
          carData: [],
          dataSource: [
            {
              trackingNumber: '合计',
              waybillQuantity: 0,
              weight: 0,
              volume: 0,
              onCollection: 0,
              advancePayment: 0,
              alreadyPay: 0,
              putPay: 0,
              backPay: 0,
            },
          ],
          alreadyChooseData: [],
          selectedRowKeys: [],
        },
      })
      yield put({
        type: 'updateunLoadList',
        payload: {
          fields: [],
          tableTime: 0,
          firmData: {},
          chooseData: {},
          carData: [],
          dataSource: [],
          alreadyChooseData: [],
          selectedRowKeys: [],
        },
      })
    },
  },
  reducers: {
    updateStates (state, { payload }) {
      return { ...state, ...payload }
    },
    updatefirmModalStates (state, { payload }) {
      let { firmModal } = state
      return { ...state, firmModal: { ...firmModal, ...payload } }
    },
    updateTransportOrderStates (state, { payload }) {
      let { transportOrder } = state
      return { ...state, transportOrder: { ...transportOrder, ...payload } }
    },
    updateCarStates (state, { payload }) {
      let { car } = state
      return { ...state, car: { ...car, ...payload } }
    },
    updateChooseModalStates (state, { payload }) {
      let { chooseModal } = state
      return { ...state, chooseModal: { ...chooseModal, ...payload } }
    },
    updateunChooseModalStates (state, { payload }) {
      let { unchooseModal } = state
      return { ...state, unchooseModal: { ...unchooseModal, ...payload } }
    },
    updateSureModalStates (state, { payload }) {
      let { sureModal } = state
      return { ...state, sureModal: { ...sureModal, ...payload } }
    },
    updateHalfwayLoadModalStates (state, { payload }) {
      let { halfwayLoadModal } = state
      return { ...state, halfwayLoadModal: { ...halfwayLoadModal, ...payload } }
    },
    updatetransportOrderHalfwayStates (state, { payload }) {
      let { transportOrderHalfway } = state
      return { ...state, transportOrderHalfway: { ...transportOrderHalfway, ...payload } }
    },
    updateCarHalfwayStates (state, { payload }) {
      let { carHalfway } = state
      return { ...state, carHalfway: { ...carHalfway, ...payload } }
    },
    updateStatusModalStates (state, { payload }) {
      let { statusModal } = state
      return { ...state, statusModal: { ...statusModal, ...payload } }
    },
    updateChooseHalfModalStates (state, { payload }) {
      let { chooseHalfModal } = state
      return { ...state, chooseHalfModal: { ...chooseHalfModal, ...payload } }
    },
    updateunChooseHalfModalStates (state, { payload }) {
      let { unchooseHalfModal } = state
      return { ...state, unchooseHalfModal: { ...unchooseHalfModal, ...payload } }
    },
    updateHalfwayunLoadModalStates (state, { payload }) {
      let { halfwayunLoadModal } = state
      return { ...state, halfwayunLoadModal: { ...halfwayunLoadModal, ...payload } }
    },
    updateCarHalfwayUnLoadStates (state, { payload }) {
      let { carHalfwayUnLoad } = state
      return { ...state, carHalfwayUnLoad: { ...carHalfwayUnLoad, ...payload } }
    },
    updateunLoadListStates (state, { payload }) {
      let { unLoadList } = state
      return { ...state, unLoadList: { ...unLoadList, ...payload } }
    },
  },
}
