package models

import "github.com/algorand/go-algorand-sdk/types"

// Account information at a given round.
// Definition:
// data/basics/userBalance.go : AccountData
type Account struct {
	// Address the account public key
	Address string `json:"address,omitempty"`

	// Amount (algo) total number of MicroAlgos in the account
	Amount uint64 `json:"amount,omitempty"`

	// AmountWithoutPendingRewards specifies the amount of MicroAlgos in the account,
	// without the pending rewards.
	AmountWithoutPendingRewards uint64 `json:"amount-without-pending-rewards,omitempty"`

	// AppsLocalState (appl) applications local data stored in this account.
	// Note the raw object uses `map[int] -> AppLocalState` for this type.
	AppsLocalState []ApplicationLocalState `json:"apps-local-state,omitempty"`

	// AppsTotalSchema (tsch) stores the sum of all of the local schemas and global
	// schemas in this account.
	// Note: the raw account uses `StateSchema` for this type.
	AppsTotalSchema ApplicationStateSchema `json:"apps-total-schema,omitempty"`

	// Assets (asset) assets held by this account.
	// Note the raw object uses `map[int] -> AssetHolding` for this type.
	Assets []AssetHolding `json:"assets,omitempty"`

	// AuthAddr (spend) the address against which signing should be checked. If empty,
	// the address of the current account is used. This field can be updated in any
	// transaction by setting the RekeyTo field.
	AuthAddr string `json:"auth-addr,omitempty"`

	// CreatedApps (appp) parameters of applications created by this account including
	// app global data.
	// Note: the raw account uses `map[int] -> AppParams` for this type.
	CreatedApps []Application `json:"created-apps,omitempty"`

	// CreatedAssets (apar) parameters of assets created by this account.
	// Note: the raw account uses `map[int] -> Asset` for this type.
	CreatedAssets []Asset `json:"created-assets,omitempty"`

	// Participation accountParticipation describes the parameters used by this account
	// in consensus protocol.
	Participation AccountParticipation `json:"participation,omitempty"`

	// PendingRewards amount of MicroAlgos of pending rewards in this account.
	PendingRewards uint64 `json:"pending-rewards,omitempty"`

	// RewardBase (ebase) used as part of the rewards computation. Only applicable to
	// accounts which are participating.
	RewardBase uint64 `json:"reward-base,omitempty"`

	// Rewards (ern) total rewards of MicroAlgos the account has received, including
	// pending rewards.
	Rewards uint64 `json:"rewards,omitempty"`

	// Round the round for which this information is relevant.
	Round uint64 `json:"round,omitempty"`

	// SigType indicates what type of signature is used by this account, must be one
	// of:
	// * sig
	// * msig
	// * lsig
	SigType string `json:"sig-type,omitempty"`

	// Status (onl) delegation status of the account's MicroAlgos
	// * Offline - indicates that the associated account is delegated.
	// * Online - indicates that the associated account used as part of the delegation
	// pool.
	// * NotParticipating - indicates that the associated account is neither a
	// delegator nor a delegate.
	Status string `json:"status,omitempty"`
}

// AccountParticipation describes the parameters used by this account in consensus
// protocol.
type AccountParticipation struct {
	// SelectionParticipationKey (sel) Selection public key (if any) currently
	// registered for this round.
	SelectionParticipationKey []byte `json:"selection-participation-key,omitempty"`

	// VoteFirstValid (voteFst) First round for which this participation is valid.
	VoteFirstValid uint64 `json:"vote-first-valid,omitempty"`

	// VoteKeyDilution (voteKD) Number of subkeys in each batch of participation keys.
	VoteKeyDilution uint64 `json:"vote-key-dilution,omitempty"`

	// VoteLastValid (voteLst) Last round for which this participation is valid.
	VoteLastValid uint64 `json:"vote-last-valid,omitempty"`

	// VoteParticipationKey (vote) root participation public key (if any) currently
	// registered for this round.
	VoteParticipationKey []byte `json:"vote-participation-key,omitempty"`
}

// Asset specifies both the unique identifier and the parameters for an asset
type Asset struct {
	// Index unique asset identifier
	Index uint64 `json:"index,omitempty"`

	// Params assetParams specifies the parameters for an asset.
	// (apar) when part of an AssetConfig transaction.
	// Definition:
	// data/transactions/asset.go : AssetParams
	Params AssetParams `json:"params,omitempty"`
}

// AssetHolding describes an asset held by an account.
// Definition:
// data/basics/userBalance.go : AssetHolding
type AssetHolding struct {
	// Amount (a) number of units held.
	Amount uint64 `json:"amount,omitempty"`

	// AssetID asset ID of the holding.
	AssetID uint64 `json:"asset-id,omitempty"`

	// Creator address that created this asset. This is the address where the
	// parameters for this asset can be found, and also the address where unwanted
	// asset units can be sent in the worst case.
	Creator string `json:"creator,omitempty"`

	// IsFrozen (f) whether or not the holding is frozen.
	IsFrozen bool `json:"is-frozen,omitempty"`
}

// AssetParams specifies the parameters for an asset.
// (apar) when part of an AssetConfig transaction.
// Definition:
// data/transactions/asset.go : AssetParams
type AssetParams struct {
	// Clawback (c) Address of account used to clawback holdings of this asset. If
	// empty, clawback is not permitted.
	Clawback string `json:"clawback,omitempty"`

	// Creator the address that created this asset. This is the address where the
	// parameters for this asset can be found, and also the address where unwanted
	// asset units can be sent in the worst case.
	Creator string `json:"creator,omitempty"`

	// Decimals (dc) The number of digits to use after the decimal point when
	// displaying this asset. If 0, the asset is not divisible. If 1, the base unit of
	// the asset is in tenths. If 2, the base unit of the asset is in hundredths, and
	// so on. This value must be between 0 and 19 (inclusive).
	Decimals uint64 `json:"decimals,omitempty"`

	// DefaultFrozen (df) Whether holdings of this asset are frozen by default.
	DefaultFrozen bool `json:"default-frozen,omitempty"`

	// Freeze (f) Address of account used to freeze holdings of this asset. If empty,
	// freezing is not permitted.
	Freeze string `json:"freeze,omitempty"`

	// Manager (m) Address of account used to manage the keys of this asset and to
	// destroy it.
	Manager string `json:"manager,omitempty"`

	// MetadataHash (am) A commitment to some unspecified asset metadata. The format of
	// this metadata is up to the application.
	MetadataHash []byte `json:"metadata-hash,omitempty"`

	// Name (an) Name of this asset, as supplied by the creator.
	Name string `json:"name,omitempty"`

	// Reserve (r) Address of account holding reserve (non-minted) units of this asset.
	Reserve string `json:"reserve,omitempty"`

	// Total (t) The total number of units of this asset.
	Total uint64 `json:"total,omitempty"`

	// UnitName (un) Name of a unit of this asset, as supplied by the creator.
	UnitName string `json:"unit-name,omitempty"`

	// Url (au) URL where more information about the asset can be retrieved.
	Url string `json:"url,omitempty"`
}

// ApplicationStateSchema specifies maximums on the number of each type that may be
// stored.
type ApplicationStateSchema struct {
	// NumByteSlice (nbs) num of byte slices.
	NumByteSlice uint64 `json:"num-byte-slice,omitempty"`

	// NumUint (nui) num of uints.
	NumUint uint64 `json:"num-uint,omitempty"`
}

// ApplicationLocalState stores local state associated with an application.
type ApplicationLocalState struct {
	// Id the application which this local state is for.
	Id uint64 `json:"id,omitempty"`

	// KeyValue (tkv) storage.
	KeyValue []TealKeyValue `json:"key-value,omitempty"`

	// Schema (hsch) schema.
	Schema ApplicationStateSchema `json:"schema,omitempty"`
}

// TealKeyValue represents a key-value pair in an application store.
type TealKeyValue struct {
	Key string `json:"key,omitempty"`

	// Value represents a TEAL value.
	Value TealValue `json:"value,omitempty"`
}

// TealValue represents a TEAL value.
type TealValue struct {
	// Bytes (tb) bytes value.
	Bytes string `json:"bytes,omitempty"`

	// Type (tt) value type.
	Type uint64 `json:"type,omitempty"`

	// Uint (ui) uint value.
	Uint uint64 `json:"uint,omitempty"`
}

// AccountStateDelta application state delta.
type AccountStateDelta struct {
	Address string `json:"address,omitempty"`

	// Delta application state delta.
	Delta []EvalDeltaKeyValue `json:"delta,omitempty"`
}

// EvalDeltaKeyValue key-value pairs for StateDelta.
type EvalDeltaKeyValue struct {
	Key string `json:"key,omitempty"`

	// Value represents a TEAL value delta.
	Value EvalDelta `json:"value,omitempty"`
}

// EvalDelta represents a TEAL value delta.
type EvalDelta struct {
	// Action (at) delta action.
	Action uint64 `json:"action,omitempty"`

	// Bytes (bs) bytes value.
	Bytes string `json:"bytes,omitempty"`

	// Uint (ui) uint value.
	Uint uint64 `json:"uint,omitempty"`
}

// Application index and its parameters
type Application struct {
	// Id (appidx) application index.
	Id uint64 `json:"id,omitempty"`

	// Params (appparams) application parameters.
	Params ApplicationParams `json:"params,omitempty"`
}

// ApplicationParams stores the global information associated with an application.
type ApplicationParams struct {
	// ApprovalProgram (approv) approval program.
	ApprovalProgram []byte `json:"approval-program,omitempty"`

	// ClearStateProgram (clearp) approval program.
	ClearStateProgram []byte `json:"clear-state-program,omitempty"`

	// Creator the address that created this application. This is the address where the
	// parameters and global state for this application can be found.
	Creator string `json:"creator,omitempty"`

	// GlobalState [\gs) global schema
	GlobalState []TealKeyValue `json:"global-state,omitempty"`

	// GlobalStateSchema [\lsch) global schema
	GlobalStateSchema ApplicationStateSchema `json:"global-state-schema,omitempty"`

	// LocalStateSchema [\lsch) local schema
	LocalStateSchema ApplicationStateSchema `json:"local-state-schema,omitempty"`
}

// DryrunState stores the TEAL eval step data
type DryrunState struct {
	// Error evaluation error if any
	Error string `json:"error,omitempty"`

	// Line number
	Line uint64 `json:"line,omitempty"`

	// Pc program counter
	Pc uint64 `json:"pc,omitempty"`

	Scratch []TealValue `json:"scratch,omitempty"`

	Stack []TealValue `json:"stack,omitempty"`
}

// DryrunTxnResult contains any LogicSig or ApplicationCall program debug
// information and state updates from a dryrun.
type DryrunTxnResult struct {
	AppCallMessages []string `json:"app-call-messages,omitempty"`

	AppCallTrace []DryrunState `json:"app-call-trace,omitempty"`

	// Disassembly disassembled program line by line.
	Disassembly []string `json:"disassembly,omitempty"`

	// GlobalDelta application state delta.
	GlobalDelta []EvalDeltaKeyValue `json:"global-delta,omitempty"`

	LocalDeltas []AccountStateDelta `json:"local-deltas,omitempty"`

	LogicSigMessages []string `json:"logic-sig-messages,omitempty"`

	LogicSigTrace []DryrunState `json:"logic-sig-trace,omitempty"`
}

// ErrorResponse an error response with optional data field.
type ErrorResponse struct {
	Data string `json:"data,omitempty"`

	Message string `json:"message,omitempty"`
}

// Version note that we annotate this as a model so that legacy clients
// can directly import a swagger generated Version model.
type Version struct {
	// Build the current algod build version information.
	Build VersionBuild `json:"build,omitempty"`

	GenesisHash []byte `json:"genesis-hash,omitempty"`

	GenesisId string `json:"genesis-id,omitempty"`

	Versions []string `json:"versions,omitempty"`
}

// VersionBuild the current algod build version information.
type VersionBuild struct {
	Branch string `json:"branch,omitempty"`

	BuildNumber uint64 `json:"build-number,omitempty"`

	Channel string `json:"channel,omitempty"`

	CommitHash []byte `json:"commit-hash,omitempty"`

	Major uint64 `json:"major,omitempty"`

	Minor uint64 `json:"minor,omitempty"`
}

// DryrunRequest request data type for dryrun endpoint. Given the Transactions and
// simulated ledger state upload, run TEAL scripts and return debugging
// information.
type DryrunRequest struct {
	Accounts []Account `json:"accounts,omitempty"`

	Apps []Application `json:"apps,omitempty"`

	// LatestTimestamp is available to some TEAL scripts. Defaults to the latest
	// confirmed timestamp this algod is attached to.
	LatestTimestamp uint64 `json:"latest-timestamp,omitempty"`

	// ProtocolVersion specifies a specific version string to operate under, otherwise
	// whatever the current protocol of the network this algod is running in.
	ProtocolVersion string `json:"protocol-version,omitempty"`

	// Round is available to some TEAL scripts. Defaults to the current round on the
	// network this algod is attached to.
	Round uint64 `json:"round,omitempty"`

	Sources []DryrunSource `json:"sources,omitempty"`

	Txns []types.SignedTxn `json:"txns,omitempty"`
}

// DryrunSource is TEAL source text that gets uploaded, compiled, and inserted into
// transactions or application state.
type DryrunSource struct {
	AppIndex uint64 `json:"app-index,omitempty"`

	// FieldName is what kind of sources this is. If lsig then it goes into the
	// transactions[this.TxnIndex].LogicSig. If approv or clearp it goes into the
	// Approval Program or Clear State Program of application[this.AppIndex].
	FieldName string `json:"field-name,omitempty"`

	Source string `json:"source,omitempty"`

	TxnIndex uint64 `json:"txn-index,omitempty"`
}

// BlockResponse encoded block object.
type BlockResponse struct {
	// Block header data.
	Block *types.SignedTxn `json:"block,omitempty"`

	// Cert optional certificate object. This is only included when the format is set
	// to message pack.
	Cert *types.SignedTxn `json:"cert,omitempty"`
}

type CatchpointStartResponse struct {
	// CatchupMessage catchup start response string
	CatchupMessage string `json:"catchup-message,omitempty"`
}

type CatchpointAbortResponse struct {
	// CatchupMessage catchup abort response string
	CatchupMessage string `json:"catchup-message,omitempty"`
}

type NodeStatusResponse struct {
	// Catchpoint the current catchpoint that is being caught up to
	Catchpoint string `json:"catchpoint,omitempty"`

	// CatchpointAcquiredBlocks the number of blocks that have already been obtained by
	// the node as part of the catchup
	CatchpointAcquiredBlocks uint64 `json:"catchpoint-acquired-blocks,omitempty"`

	// CatchpointProcessedAccounts the number of account from the current catchpoint
	// that have been processed so far as part of the catchup
	CatchpointProcessedAccounts uint64 `json:"catchpoint-processed-accounts,omitempty"`

	// CatchpointTotalAccounts the total number of accounts included in the current
	// catchpoint
	CatchpointTotalAccounts uint64 `json:"catchpoint-total-accounts,omitempty"`

	// CatchpointTotalBlocks the total number of blocks that are required to complete
	// the current catchpoint catchup
	CatchpointTotalBlocks uint64 `json:"catchpoint-total-blocks,omitempty"`

	// CatchupTime in nanoseconds
	CatchupTime uint64 `json:"catchup-time,omitempty"`

	// LastCatchpoint the last catchpoint seen by the node
	LastCatchpoint string `json:"last-catchpoint,omitempty"`

	// LastRound indicates the last round seen
	LastRound uint64 `json:"last-round,omitempty"`

	// LastVersion indicates the last consensus version supported
	LastVersion string `json:"last-version,omitempty"`

	// NextVersion of consensus protocol to use
	NextVersion string `json:"next-version,omitempty"`

	// NextVersionRound is the round at which the next consensus version will apply
	NextVersionRound uint64 `json:"next-version-round,omitempty"`

	// NextVersionSupported indicates whether the next consensus version is supported
	// by this node
	NextVersionSupported bool `json:"next-version-supported,omitempty"`

	// StoppedAtUnsupportedRound indicates that the node does not support the new
	// rounds and has stopped making progress
	StoppedAtUnsupportedRound bool `json:"stopped-at-unsupported-round,omitempty"`

	// TimeSinceLastRound in nanoseconds
	TimeSinceLastRound uint64 `json:"time-since-last-round,omitempty"`
}

// PendingTransactionResponse given a transaction id of a recently submitted
// transaction, it returns information about it. There are several cases when this
// might succeed:
// - transaction committed (committed round > 0)
// - transaction still in the pool (committed round = 0, pool error = "")
// - transaction removed from pool due to error (committed round = 0, pool error !=
// "")
// Or the transaction may have happened sufficiently long ago that the node no
// longer remembers it, and this will return an error.
type PendingTransactionResponse struct {
	// AssetIndex the asset index if the transaction was found and it created an asset.
	AssetIndex uint64 `json:"asset-index,omitempty"`

	// CloseRewards rewards in microalgos applied to the close remainder to account.
	CloseRewards uint64 `json:"close-rewards,omitempty"`

	// ClosingAmount closing amount for the transaction.
	ClosingAmount uint64 `json:"closing-amount,omitempty"`

	// ConfirmedRound the round where this transaction was confirmed, if present.
	ConfirmedRound uint64 `json:"confirmed-round,omitempty"`

	// PoolError indicates that the transaction was kicked out of this node's
	// transaction pool (and specifies why that happened). An empty string indicates
	// the transaction wasn't kicked out of this node's txpool due to an error.
	PoolError string `json:"pool-error,omitempty"`

	// ReceiverRewards rewards in microalgos applied to the receiver account.
	ReceiverRewards uint64 `json:"receiver-rewards,omitempty"`

	// SenderRewards rewards in microalgos applied to the sender account.
	SenderRewards uint64 `json:"sender-rewards,omitempty"`

	// Txn the raw signed transaction.
	Txn *types.SignedTxn `json:"txn,omitempty"`
}

// PendingTransactionsResponse a potentially truncated list of transactions
// currently in the node's transaction pool. You can compute whether or not the
// list is truncated if the number of elements in the **top-transactions** array is
// fewer than **total-transactions**.
type PendingTransactionsResponse struct {
	// TopTransactions an array of signed transaction objects.
	TopTransactions []types.SignedTxn `json:"top-transactions,omitempty"`

	// TotalTransactions total number of transactions in the pool.
	TotalTransactions uint64 `json:"total-transactions,omitempty"`
}

// PostTransactionsResponse transaction ID of the submission.
type PostTransactionsResponse struct {
	// TxId encoding of the transaction hash.
	TxId string `json:"txId,omitempty"`
}

// SupplyResponse supply represents the current supply of MicroAlgos in the system.
type SupplyResponse struct {
	// Current_round round
	Current_round uint64 `json:"current_round,omitempty"`

	// OnlineMoney
	OnlineMoney uint64 `json:"online-money,omitempty"`

	// TotalMoney
	TotalMoney uint64 `json:"total-money,omitempty"`
}

// TransactionParametersResponse transactionParams contains the parameters that
// help a client construct a new transaction.
type TransactionParametersResponse struct {
	// ConsensusVersion indicates the consensus protocol version
	// as of LastRound.
	ConsensusVersion string `json:"consensus-version,omitempty"`

	// Fee is the suggested transaction fee
	// Fee is in units of micro-Algos per byte.
	// Fee may fall to zero but transactions must still have a fee of
	// at least MinTxnFee for the current network protocol.
	Fee uint64 `json:"fee,omitempty"`

	// GenesisHash is the hash of the genesis block.
	GenesisHash []byte `json:"genesis-hash,omitempty"`

	// GenesisId genesisID is an ID listed in the genesis block.
	GenesisId string `json:"genesis-id,omitempty"`

	// LastRound indicates the last round seen
	LastRound uint64 `json:"last-round,omitempty"`

	// MinFee the minimum transaction fee (not per byte) required for the
	// txn to validate for the current network protocol.
	MinFee uint64 `json:"min-fee,omitempty"`
}

// CompileResponse teal compile Result
type CompileResponse struct {
	// Hash base32 SHA512_256 of program bytes (Address style)
	Hash string `json:"hash,omitempty"`

	// Result base64 encoded program bytes
	Result string `json:"result,omitempty"`
}

// DryrunResponse contains per-txn debug information from a dryrun.
type DryrunResponse struct {
	Error string `json:"error,omitempty"`

	// ProtocolVersion protocol version is the protocol version Dryrun was operated
	// under.
	ProtocolVersion string `json:"protocol-version,omitempty"`

	Txns []DryrunTxnResult `json:"txns,omitempty"`
}
// Block information.
// Definition:
// data/bookkeeping/block.go : Block
type Block struct {
	// GenesisHash (gh) hash to which this block belongs.
	GenesisHash []byte `json:"genesis-hash,omitempty"`

	// GenesisId (gen) ID to which this block belongs.
	GenesisId string `json:"genesis-id,omitempty"`

	// PreviousBlockHash (prev) Previous block hash.
	PreviousBlockHash []byte `json:"previous-block-hash,omitempty"`

	// Rewards fields relating to rewards,
	Rewards BlockRewards `json:"rewards,omitempty"`

	// Round (rnd) Current round on which this block was appended to the chain.
	Round uint64 `json:"round,omitempty"`

	// Seed (seed) Sortition seed.
	Seed []byte `json:"seed,omitempty"`

	// Timestamp (ts) Block creation timestamp in seconds since eposh
	Timestamp uint64 `json:"timestamp,omitempty"`

	// Transactions (txns) list of transactions corresponding to a given round.
	Transactions []Transaction `json:"transactions,omitempty"`

	// TransactionsRoot (txn) TransactionsRoot authenticates the set of transactions
	// appearing in the block. More specifically, it's the root of a merkle tree whose
	// leaves are the block's Txids, in lexicographic order. For the empty block, it's
	// 0. Note that the TxnRoot does not authenticate the signatures on the
	// transactions, only the transactions themselves. Two blocks with the same
	// transactions but in a different order and with different signatures will have
	// the same TxnRoot.
	TransactionsRoot []byte `json:"transactions-root,omitempty"`

	// TxnCounter (tc) TxnCounter counts the number of transactions committed in the
	// ledger, from the time at which support for this feature was introduced.
	// Specifically, TxnCounter is the number of the next transaction that will be
	// committed after this block. It is 0 when no transactions have ever been
	// committed (since TxnCounter started being supported).
	TxnCounter uint64 `json:"txn-counter,omitempty"`

	// UpgradeState fields relating to a protocol upgrade.
	UpgradeState BlockUpgradeState `json:"upgrade-state,omitempty"`

	// UpgradeVote fields relating to voting for a protocol upgrade.
	UpgradeVote BlockUpgradeVote `json:"upgrade-vote,omitempty"`
}

// BlockRewards fields relating to rewards,
type BlockRewards struct {
	// FeeSink (fees) accepts transaction fees, it can only spend to the incentive
	// pool.
	FeeSink string `json:"fee-sink,omitempty"`

	// RewardsCalculationRound (rwcalr) number of leftover MicroAlgos after the
	// distribution of rewards-rate MicroAlgos for every reward unit in the next round.
	RewardsCalculationRound uint64 `json:"rewards-calculation-round,omitempty"`

	// RewardsLevel (earn) How many rewards, in MicroAlgos, have been distributed to
	// each RewardUnit of MicroAlgos since genesis.
	RewardsLevel uint64 `json:"rewards-level,omitempty"`

	// RewardsPool (rwd) accepts periodic injections from the fee-sink and continually
	// redistributes them as rewards.
	RewardsPool string `json:"rewards-pool,omitempty"`

	// RewardsRate (rate) Number of new MicroAlgos added to the participation stake
	// from rewards at the next round.
	RewardsRate uint64 `json:"rewards-rate,omitempty"`

	// RewardsResidue (frac) Number of leftover MicroAlgos after the distribution of
	// RewardsRate/rewardUnits MicroAlgos for every reward unit in the next round.
	RewardsResidue uint64 `json:"rewards-residue,omitempty"`
}

// BlockUpgradeState fields relating to a protocol upgrade.
type BlockUpgradeState struct {
	// CurrentProtocol (proto) The current protocol version.
	CurrentProtocol string `json:"current-protocol,omitempty"`

	// NextProtocol (nextproto) The next proposed protocol version.
	NextProtocol string `json:"next-protocol,omitempty"`

	// NextProtocolApprovals (nextyes) Number of blocks which approved the protocol
	// upgrade.
	NextProtocolApprovals uint64 `json:"next-protocol-approvals,omitempty"`

	// NextProtocolSwitchOn (nextswitch) Round on which the protocol upgrade will take
	// effect.
	NextProtocolSwitchOn uint64 `json:"next-protocol-switch-on,omitempty"`

	// NextProtocolVoteBefore (nextbefore) Deadline round for this protocol upgrade (No
	// votes will be consider after this round).
	NextProtocolVoteBefore uint64 `json:"next-protocol-vote-before,omitempty"`
}

// BlockUpgradeVote fields relating to voting for a protocol upgrade.
type BlockUpgradeVote struct {
	// UpgradeApprove (upgradeyes) Indicates a yes vote for the current proposal.
	UpgradeApprove bool `json:"upgrade-approve,omitempty"`

	// UpgradeDelay (upgradedelay) Indicates the time between acceptance and execution.
	UpgradeDelay uint64 `json:"upgrade-delay,omitempty"`

	// UpgradePropose (upgradeprop) Indicates a proposed upgrade.
	UpgradePropose string `json:"upgrade-propose,omitempty"`
}

// HealthCheck a health check response.
type HealthCheck struct {
	Data *types.SignedTxn `json:"data,omitempty"`

	Message string `json:"message,omitempty"`
}

// MiniAssetHolding a simplified version of AssetHolding
type MiniAssetHolding struct {
	Address string `json:"address,omitempty"`

	Amount uint64 `json:"amount,omitempty"`

	IsFrozen bool `json:"is-frozen,omitempty"`
}

// StateSchema represents a (apls) local-state or (apgs) global-state schema. These
// schemas determine how much storage may be used in a local-state or global-state
// for an application. The more space used, the larger minimum balance must be
// maintained in the account holding the data.
type StateSchema struct {
	// NumByteSlice maximum number of TEAL byte slices that may be stored in the
	// key/value store.
	NumByteSlice uint64 `json:"num-byte-slice,omitempty"`

	// NumUint maximum number of TEAL uints that may be stored in the key/value store.
	NumUint uint64 `json:"num-uint,omitempty"`
}

// Transaction contains all fields common to all transactions and serves as an
// envelope to all transactions type.
// Definition:
// data/transactions/signedtxn.go : SignedTxn
// data/transactions/transaction.go : Transaction
type Transaction struct {
	// ApplicationTransaction fields for application transactions.
	// Definition:
	// data/transactions/application.go : ApplicationCallTxnFields
	ApplicationTransaction TransactionApplication `json:"application-transaction,omitempty"`

	// AssetConfigTransaction fields for asset allocation, re-configuration, and
	// destruction.
	// A zero value for asset-id indicates asset creation.
	// A zero value for the params indicates asset destruction.
	// Definition:
	// data/transactions/asset.go : AssetConfigTxnFields
	AssetConfigTransaction TransactionAssetConfig `json:"asset-config-transaction,omitempty"`

	// AssetFreezeTransaction fields for an asset freeze transaction.
	// Definition:
	// data/transactions/asset.go : AssetFreezeTxnFields
	AssetFreezeTransaction TransactionAssetFreeze `json:"asset-freeze-transaction,omitempty"`

	// AssetTransferTransaction fields for an asset transfer transaction.
	// Definition:
	// data/transactions/asset.go : AssetTransferTxnFields
	AssetTransferTransaction TransactionAssetTransfer `json:"asset-transfer-transaction,omitempty"`

	// AuthAddr (sgnr) The address used to sign the transaction. This is used for
	// rekeyed accounts to indicate that the sender address did not sign the
	// transaction.
	AuthAddr string `json:"auth-addr,omitempty"`

	// CloseRewards (rc) rewards applied to close-remainder-to account.
	CloseRewards uint64 `json:"close-rewards,omitempty"`

	// ClosingAmount (ca) closing amount for transaction.
	ClosingAmount uint64 `json:"closing-amount,omitempty"`

	// ConfirmedRound round when the transaction was confirmed.
	ConfirmedRound uint64 `json:"confirmed-round,omitempty"`

	// CreatedApplicationIndex specifies an application index (ID) if an application
	// was created with this transaction.
	CreatedApplicationIndex uint64 `json:"created-application-index,omitempty"`

	// CreatedAssetIndex specifies an asset index (ID) if an asset was created with
	// this transaction.
	CreatedAssetIndex uint64 `json:"created-asset-index,omitempty"`

	// Fee (fee) Transaction fee.
	Fee uint64 `json:"fee,omitempty"`

	// FirstValid (fv) First valid round for this transaction.
	FirstValid uint64 `json:"first-valid,omitempty"`

	// GenesisHash (gh) Hash of genesis block.
	GenesisHash []byte `json:"genesis-hash,omitempty"`

	// GenesisId (gen) genesis block ID.
	GenesisId string `json:"genesis-id,omitempty"`

	// GlobalStateDelta (gd) Global state key/value changes for the application being
	// executed by this transaction.
	GlobalStateDelta []EvalDeltaKeyValue `json:"global-state-delta,omitempty"`

	// Group (grp) Base64 encoded byte array of a sha512/256 digest. When present
	// indicates that this transaction is part of a transaction group and the value is
	// the sha512/256 hash of the transactions in that group.
	Group []byte `json:"group,omitempty"`

	// Id transaction ID
	Id string `json:"id,omitempty"`

	// IntraRoundOffset offset into the round where this transaction was confirmed.
	IntraRoundOffset uint64 `json:"intra-round-offset,omitempty"`

	// KeyregTransaction fields for a keyreg transaction.
	// Definition:
	// data/transactions/keyreg.go : KeyregTxnFields
	KeyregTransaction TransactionKeyreg `json:"keyreg-transaction,omitempty"`

	// LastValid (lv) Last valid round for this transaction.
	LastValid uint64 `json:"last-valid,omitempty"`

	// Lease (lx) Base64 encoded 32-byte array. Lease enforces mutual exclusion of
	// transactions. If this field is nonzero, then once the transaction is confirmed,
	// it acquires the lease identified by the (Sender, Lease) pair of the transaction
	// until the LastValid round passes. While this transaction possesses the lease, no
	// other transaction specifying this lease can be confirmed.
	Lease []byte `json:"lease,omitempty"`

	// LocalStateDelta (ld) Local state key/value changes for the application being
	// executed by this transaction.
	LocalStateDelta []AccountStateDelta `json:"local-state-delta,omitempty"`

	// Note (note) Free form data.
	Note []byte `json:"note,omitempty"`

	// PaymentTransaction fields for a payment transaction.
	// Definition:
	// data/transactions/payment.go : PaymentTxnFields
	PaymentTransaction TransactionPayment `json:"payment-transaction,omitempty"`

	// ReceiverRewards (rr) rewards applied to receiver account.
	ReceiverRewards uint64 `json:"receiver-rewards,omitempty"`

	// RekeyTo (rekey) when included in a valid transaction, the accounts auth addr
	// will be updated with this value and future signatures must be signed with the
	// key represented by this address.
	RekeyTo string `json:"rekey-to,omitempty"`

	// RoundTime time when the block this transaction is in was confirmed.
	RoundTime uint64 `json:"round-time,omitempty"`

	// Sender (snd) Sender's address.
	Sender string `json:"sender,omitempty"`

	// SenderRewards (rs) rewards applied to sender account.
	SenderRewards uint64 `json:"sender-rewards,omitempty"`

	// Signature validation signature associated with some data. Only one of the
	// signatures should be provided.
	Signature TransactionSignature `json:"signature,omitempty"`

	// TxType (type) Indicates what type of transaction this is. Different types have
	// different fields.
	// Valid types, and where their fields are stored:
	// * (pay) payment-transaction
	// * (keyreg) keyreg-transaction
	// * (acfg) asset-config-transaction
	// * (axfer) asset-transfer-transaction
	// * (afrz) asset-freeze-transaction
	// * (appl) application-transaction
	TxType string `json:"tx-type,omitempty"`
}

// TransactionApplication fields for application transactions.
// Definition:
// data/transactions/application.go : ApplicationCallTxnFields
type TransactionApplication struct {
	// Accounts (apat) List of accounts in addition to the sender that may be accessed
	// from the application's approval-program and clear-state-program.
	Accounts []string `json:"accounts,omitempty"`

	// ApplicationArgs (apaa) transaction specific arguments accessed from the
	// application's approval-program and clear-state-program.
	ApplicationArgs [][]byte `json:"application-args,omitempty"`

	// ApplicationId (apid) ID of the application being configured or empty if
	// creating.
	ApplicationId uint64 `json:"application-id,omitempty"`

	// ApprovalProgram (apap) Logic executed for every application transaction, except
	// when on-completion is set to "clear". It can read and write global state for the
	// application, as well as account-specific local state. Approval programs may
	// reject the transaction.
	ApprovalProgram []byte `json:"approval-program,omitempty"`

	// ClearStateProgram (apsu) Logic executed for application transactions with
	// on-completion set to "clear". It can read and write global state for the
	// application, as well as account-specific local state. Clear state programs
	// cannot reject the transaction.
	ClearStateProgram []byte `json:"clear-state-program,omitempty"`

	// ForeignApps (apfa) Lists the applications in addition to the application-id
	// whose global states may be accessed by this application's approval-program and
	// clear-state-program. The access is read-only.
	ForeignApps []uint64 `json:"foreign-apps,omitempty"`

	// GlobalStateSchema represents a (apls) local-state or (apgs) global-state schema.
	// These schemas determine how much storage may be used in a local-state or
	// global-state for an application. The more space used, the larger minimum balance
	// must be maintained in the account holding the data.
	GlobalStateSchema StateSchema `json:"global-state-schema,omitempty"`

	// LocalStateSchema represents a (apls) local-state or (apgs) global-state schema.
	// These schemas determine how much storage may be used in a local-state or
	// global-state for an application. The more space used, the larger minimum balance
	// must be maintained in the account holding the data.
	LocalStateSchema StateSchema `json:"local-state-schema,omitempty"`

	// OnCompletion (apan) defines the what additional actions occur with the
	// transaction.
	// Valid types:
	// * noop
	// * optin
	// * closeout
	// * clear
	// * update
	// * update
	// * delete
	OnCompletion string `json:"on-completion,omitempty"`
}

// TransactionAssetConfig fields for asset allocation, re-configuration, and
// destruction.
// A zero value for asset-id indicates asset creation.
// A zero value for the params indicates asset destruction.
// Definition:
// data/transactions/asset.go : AssetConfigTxnFields
type TransactionAssetConfig struct {
	// AssetId (xaid) ID of the asset being configured or empty if creating.
	AssetId uint64 `json:"asset-id,omitempty"`

	// Params assetParams specifies the parameters for an asset.
	// (apar) when part of an AssetConfig transaction.
	// Definition:
	// data/transactions/asset.go : AssetParams
	Params AssetParams `json:"params,omitempty"`
}

// TransactionAssetFreeze fields for an asset freeze transaction.
// Definition:
// data/transactions/asset.go : AssetFreezeTxnFields
type TransactionAssetFreeze struct {
	// Address (fadd) Address of the account whose asset is being frozen or thawed.
	Address string `json:"address,omitempty"`

	// AssetId (faid) ID of the asset being frozen or thawed.
	AssetId uint64 `json:"asset-id,omitempty"`

	// NewFreezeStatus (afrz) The new freeze status.
	NewFreezeStatus bool `json:"new-freeze-status,omitempty"`
}

// TransactionAssetTransfer fields for an asset transfer transaction.
// Definition:
// data/transactions/asset.go : AssetTransferTxnFields
type TransactionAssetTransfer struct {
	// Amount (aamt) Amount of asset to transfer. A zero amount transferred to self
	// allocates that asset in the account's Assets map.
	Amount uint64 `json:"amount,omitempty"`

	// AssetId (xaid) ID of the asset being transferred.
	AssetId uint64 `json:"asset-id,omitempty"`

	// CloseAmount number of assets transfered to the close-to account as part of the
	// transaction.
	CloseAmount uint64 `json:"close-amount,omitempty"`

	// CloseTo (aclose) Indicates that the asset should be removed from the account's
	// Assets map, and specifies where the remaining asset holdings should be
	// transferred. It's always valid to transfer remaining asset holdings to the
	// creator account.
	CloseTo string `json:"close-to,omitempty"`

	// Receiver (arcv) Recipient address of the transfer.
	Receiver string `json:"receiver,omitempty"`

	// Sender (asnd) The effective sender during a clawback transactions. If this is
	// not a zero value, the real transaction sender must be the Clawback address from
	// the AssetParams.
	Sender string `json:"sender,omitempty"`
}

// TransactionKeyreg fields for a keyreg transaction.
// Definition:
// data/transactions/keyreg.go : KeyregTxnFields
type TransactionKeyreg struct {
	// NonParticipation (nonpart) Mark the account as participating or
	// non-participating.
	NonParticipation bool `json:"non-participation,omitempty"`

	// SelectionParticipationKey (selkey) Public key used with the Verified Random
	// Function (VRF) result during committee selection.
	SelectionParticipationKey []byte `json:"selection-participation-key,omitempty"`

	// VoteFirstValid (votefst) First round this participation key is valid.
	VoteFirstValid uint64 `json:"vote-first-valid,omitempty"`

	// VoteKeyDilution (votekd) Number of subkeys in each batch of participation keys.
	VoteKeyDilution uint64 `json:"vote-key-dilution,omitempty"`

	// VoteLastValid (votelst) Last round this participation key is valid.
	VoteLastValid uint64 `json:"vote-last-valid,omitempty"`

	// VoteParticipationKey (votekey) Participation public key used in key registration
	// transactions.
	VoteParticipationKey []byte `json:"vote-participation-key,omitempty"`
}

// TransactionPayment fields for a payment transaction.
// Definition:
// data/transactions/payment.go : PaymentTxnFields
type TransactionPayment struct {
	// Amount (amt) number of MicroAlgos intended to be transferred.
	Amount uint64 `json:"amount,omitempty"`

	// CloseAmount number of MicroAlgos that were sent to the close-remainder-to
	// address when closing the sender account.
	CloseAmount uint64 `json:"close-amount,omitempty"`

	// CloseRemainderTo (close) when set, indicates that the sending account should be
	// closed and all remaining funds be transferred to this address.
	CloseRemainderTo string `json:"close-remainder-to,omitempty"`

	// Receiver (rcv) receiver's address.
	Receiver string `json:"receiver,omitempty"`
}

// TransactionSignature validation signature associated with some data. Only one of
// the signatures should be provided.
type TransactionSignature struct {
	// Logicsig (lsig) Programatic transaction signature.
	// Definition:
	// data/transactions/logicsig.go
	Logicsig TransactionSignatureLogicsig `json:"logicsig,omitempty"`

	// Multisig (msig) structure holding multiple subsignatures.
	// Definition:
	// crypto/multisig.go : MultisigSig
	Multisig TransactionSignatureMultisig `json:"multisig,omitempty"`

	// Sig (sig) Standard ed25519 signature.
	Sig []byte `json:"sig,omitempty"`
}

// TransactionSignatureLogicsig (lsig) Programatic transaction signature.
// Definition:
// data/transactions/logicsig.go
type TransactionSignatureLogicsig struct {
	// Args (arg) Logic arguments, base64 encoded.
	Args [][]byte `json:"args,omitempty"`

	// Logic (l) Program signed by a signature or multi signature, or hashed to be the
	// address of ana ccount. Base64 encoded TEAL program.
	Logic []byte `json:"logic,omitempty"`

	// MultisigSignature (msig) structure holding multiple subsignatures.
	// Definition:
	// crypto/multisig.go : MultisigSig
	MultisigSignature TransactionSignatureMultisig `json:"multisig-signature,omitempty"`

	// Signature (sig) ed25519 signature.
	Signature []byte `json:"signature,omitempty"`
}

// TransactionSignatureMultisig (msig) structure holding multiple subsignatures.
// Definition:
// crypto/multisig.go : MultisigSig
type TransactionSignatureMultisig struct {
	// Subsignature (subsig) holds pairs of public key and signatures.
	Subsignature []TransactionSignatureMultisigSubsignature `json:"subsignature,omitempty"`

	// Threshold (thr)
	Threshold uint64 `json:"threshold,omitempty"`

	// Version (v)
	Version uint64 `json:"version,omitempty"`
}

type TransactionSignatureMultisigSubsignature struct {
	// PublicKey (pk)
	PublicKey []byte `json:"public-key,omitempty"`

	// Signature (s)
	Signature []byte `json:"signature,omitempty"`
}

type AccountResponse struct {
	// Account information at a given round.
	// Definition:
	// data/basics/userBalance.go : AccountData
	Account Account `json:"account,omitempty"`

	// CurrentRound round at which the results were computed.
	CurrentRound uint64 `json:"current-round,omitempty"`
}

type AccountsResponse struct {
	Accounts []Account `json:"accounts,omitempty"`

	// CurrentRound round at which the results were computed.
	CurrentRound uint64 `json:"current-round,omitempty"`

	// NextToken used for pagination, when making another request provide this token
	// with the next parameter.
	NextToken string `json:"next-token,omitempty"`
}

type AssetBalancesResponse struct {
	Balances []MiniAssetHolding `json:"balances,omitempty"`

	// CurrentRound round at which the results were computed.
	CurrentRound uint64 `json:"current-round,omitempty"`

	// NextToken used for pagination, when making another request provide this token
	// with the next parameter.
	NextToken string `json:"next-token,omitempty"`
}

type ApplicationResponse struct {
	// Application index and its parameters
	Application Application `json:"application,omitempty"`

	// CurrentRound round at which the results were computed.
	CurrentRound uint64 `json:"current-round,omitempty"`
}

type ApplicationsResponse struct {
	Applications []Application `json:"applications,omitempty"`

	// CurrentRound round at which the results were computed.
	CurrentRound uint64 `json:"current-round,omitempty"`

	// NextToken used for pagination, when making another request provide this token
	// with the next parameter.
	NextToken string `json:"next-token,omitempty"`
}

type AssetResponse struct {
	// Asset specifies both the unique identifier and the parameters for an asset
	Asset Asset `json:"asset,omitempty"`

	// CurrentRound round at which the results were computed.
	CurrentRound uint64 `json:"current-round,omitempty"`
}

type AssetsResponse struct {
	Assets []Asset `json:"assets,omitempty"`

	// CurrentRound round at which the results were computed.
	CurrentRound uint64 `json:"current-round,omitempty"`

	// NextToken used for pagination, when making another request provide this token
	// with the next parameter.
	NextToken string `json:"next-token,omitempty"`
}

type TransactionsResponse struct {
	// CurrentRound round at which the results were computed.
	CurrentRound uint64 `json:"current-round,omitempty"`

	// NextToken used for pagination, when making another request provide this token
	// with the next parameter.
	NextToken string `json:"next-token,omitempty"`

	Transactions []Transaction `json:"transactions,omitempty"`
}