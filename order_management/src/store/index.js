import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    socketMsg: false
  },
  mutations: {
    setSocketMsg(state, val) {
      state.socketMsg = val
    },
  },
  actions: {
    setSocketMsg (context) {
      context.commit('setSocketMsg', true)
    },
    resetSocketMsg(context) {
      context.commit('setSocketMsg', false)
    }
  }
})
